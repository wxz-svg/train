package com.wxz.train.generator.server;

import com.wxz.train.generator.utils.DbUtils;
import com.wxz.train.generator.utils.Field;
import com.wxz.train.generator.utils.FreemarkerUtil;
import freemarker.template.TemplateException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ServerGenerator {
    static boolean readOnly = true;
    //static String vuePath = "web/src/views/main/";
    static String vuePath = "admin/src/views/main/";
    static String serverPath = "[module]/src/main/java/com/wxz/train/[module]/";
    static String pomPath = "generator\\pom.xml";
    static String module = "";

    static {
        new File(serverPath).mkdirs();
    }

    public static void main(String[] args) throws Exception {
        // 获取mybatis-generator
        String generatorPath = getGeneratorPath();
        // 比如generator-config-member.xml，得到module = member
        module = generatorPath.replace("src/main/resources/generator-config-", "").replace(".xml", "");
        System.out.println("module: " + module);
        serverPath = serverPath.replace("[module]", module);
        // new File(servicePath).mkdirs();
        System.out.println("servicePath: " + serverPath);

        // 读取table节点
        Document document = new SAXReader().read("generator/" + generatorPath);
        Node table = document.selectSingleNode("//table");
        System.out.println(table);
        Node tableName = table.selectSingleNode("@tableName");
        Node domainObjectName = table.selectSingleNode("@domainObjectName");
        System.out.println(tableName.getText() + "/" + domainObjectName.getText());

        // 为DbUtil设置数据源
        Node connectionURL = document.selectSingleNode("//@connectionURL");
        Node userId = document.selectSingleNode("//@userId");
        Node password = document.selectSingleNode("//@password");
        System.out.println("url: " + connectionURL.getText());
        System.out.println("user: " + userId.getText());
        System.out.println("password: " + password.getText());
        DbUtils.url = connectionURL.getText();
        DbUtils.user = userId.getText();
        DbUtils.password = password.getText();

        // 示例：表名 demo_test
        // Domain = DemoTest
        String Domain = domainObjectName.getText();
        // domain = demoTest
        String domain = Domain.substring(0, 1).toLowerCase() + Domain.substring(1);
        // do_main = demo-test
        String do_main = tableName.getText().replaceAll("_", "-");
        // 表中文名
        String tableNameCn = DbUtils.getTableComment(tableName.getText());
        List<Field> fieldList = DbUtils.getColumnByTableName(tableName.getText());
        Set<String> typeSet = getJavaTypes(fieldList);

        // 组装参数
        Map<String, Object> param = new HashMap<>();
        param.put("module", module);
        param.put("Domain", Domain);
        param.put("domain", domain);
        param.put("do_main", do_main);
        param.put("tableNameCn", tableNameCn);
        param.put("fieldList", fieldList);
        param.put("typeSet", typeSet);
        param.put("readOnly", readOnly);
        System.out.println("组装参数：" + param);

        gen(Domain, param, "service", "service");
        gen(Domain, param, "controller/admin", "adminController");
        //gen(Domain, param, "controller", "controller");
        gen(Domain, param, "req", "saveReq");
        gen(Domain, param, "req", "queryReq");
        gen(Domain, param, "resp", "queryResp");
        genVue(do_main, param);
    }

    /**
     * 生成Java文件的工具方法。
     *
     * @param Domain 生成文件的域名部分
     * @param param 传递给模板引擎的参数
     * @param packageName 生成文件所在的包名
     * @param target 生成文件的目标类名
     * @throws IOException 当读写文件发生错误时抛出
     * @throws TemplateException 当处理模板发生错误时抛出
     */
    private static void gen(String Domain, Map<String, Object> param, String packageName, String target) throws IOException, TemplateException {
        // 初始化Freemarker模板配置
        FreemarkerUtil.initConfig(target + ".ftl");
        // 计算生成文件的完整路径
        String toPath = serverPath + packageName + "/";
        // 确保目录存在
        new File(toPath).mkdirs();
        // 生成类名
        String Target = target.substring(0, 1).toUpperCase() + target.substring(1);
        // 完整的文件名
        String fileName = toPath + Domain + Target + ".java";
        // 输出开始生成的提示
        System.out.println("开始生成：" + fileName);
        // 使用Freemarker生成文件
        FreemarkerUtil.generator(fileName, param);
    }

    /**
     * 生成Vue组件文件的工具方法。
     *
     * @param do_main 组件名
     * @param param 传递给模板引擎的参数
     * @throws IOException 当读写文件发生错误时抛出
     * @throws TemplateException 当处理模板发生错误时抛出
     */
    private static void genVue(String do_main, Map<String, Object> param) throws IOException, TemplateException {
        // 初始化Freemarker模板配置
        FreemarkerUtil.initConfig("vue.ftl");
        // 确保目录存在
        new File(vuePath + module).mkdirs();
        // 计算生成文件的完整路径
        String fileName = vuePath + module + "/" + do_main + ".vue";
        // 输出开始生成的提示
        System.out.println("开始生成：" + fileName);
        // 使用Freemarker生成文件
        FreemarkerUtil.generator(fileName, param);
    }

    /**
     * 获取生成器路径。
     * 此方法通过读取POM文件中的configurationFile节点的值，来获取生成器的路径。
     * 注意：此方法假设pomPath已经定义且指向一个有效的POM文件。
     *
     * @return String 代表生成器路径的字符串。
     * @throws DocumentException 如果读取XML文档时发生错误。
     */
    private static String getGeneratorPath() throws DocumentException {
        // 创建SAXReader用于读取XML文档
        SAXReader saxReader = new SAXReader();

        // 定义命名空间映射以便正确解析XPath
        Map<String, String> map = new HashMap<String, String>();
        map.put("pom", "http://maven.apache.org/POM/4.0.0");

        // 设置命名空间映射到文档工厂
        saxReader.getDocumentFactory().setXPathNamespaceURIs(map);

        // 读取POM文件为Document对象
        Document document = saxReader.read(pomPath);

        // 使用XPath查询获取configurationFile节点
        Node node = document.selectSingleNode("//pom:configurationFile");

        // 打印并返回configurationFile节点的文本内容
        System.out.println(node.getText());
        return node.getText();
    }

    /**
     * 获取所有的Java类型，使用Set去重
     * @param fieldList 包含待处理字段的列表
     * @return 返回一个包含所有不重复Java类型的集合
     */
    private static Set<String> getJavaTypes(List<Field> fieldList) {
        // 使用HashSet存储唯一类型的字符串
        Set<String> set = new HashSet<>();
        // 遍历字段列表以获取各自的Java类型
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            // 将字段的Java类型添加到集合中
            set.add(field.getJavaType());
        }
        return set;
    }

}