package com.wxz.train.generator.server;

import com.wxz.train.generator.utils.DbUtils;
import com.wxz.train.generator.utils.Field;
import com.wxz.train.generator.utils.FreeMakerUtil;
import freemarker.template.TemplateException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 服务器代码生成器，利用FreeMarker模板生成指定名称的Java类。
 */
public class ServerGenerator {

    // 目标文件路径，生成的Java文件将保存在此路径下
    static String servicePath = "[module]/src/main/java/com/wxz/train/[module]/";

    static String pomPath = "generator\\pom.xml";

    // 在类加载时创建目标路径下的文件夹，确保路径存在
    static {
        new File(servicePath).mkdirs();
    }

    /**
     * 主函数，用于执行代码生成的流程。
     *
     * @param args 命令行参数
     * @throws Exception 可能抛出的异常，包括文件读写异常和XML解析异常等
     */
    public static void main(String[] args) throws Exception {
        // 获取生成器路径-->mybatis-generator
        String generatorPath = getGeneratorPath();

        // 获取模块名称-->module,比如generator-config-member.xml,得到member
        String module = generatorPath.replace("src/main/resources/generator-config-", "").replace(".xml", "");
        System.out.println("module: "+ module);

        // 替换生成器路径中的模块名称
        servicePath = servicePath.replace("[module]", module);
        System.out.println("servicePath: "+ servicePath);

        // 使用SAXReader读取并解析XML文件,读取table节点
        Document document = new SAXReader().read("generator/" + generatorPath);

        // 定位到table节点
        Node table = document.selectSingleNode("//table");
        System.out.println(table);

        // 获取table节点的tableName和domainObjectName属性值
        Node tableName = table.selectSingleNode("@tableName");
        Node domainObjectName = table.selectSingleNode("@domainObjectName");

        // 输出属性值
        System.out.println(tableName.getText() + "/" + domainObjectName.getText());

        // 为DbUtils设置数据源
        Node connectionURL = document.selectSingleNode("//@connectionURL");
        Node userId = document.selectSingleNode("//@userId");
        Node password = document.selectSingleNode("//@password");
        // 打印配置信息，用于日志或调试
        System.out.println("url: " + connectionURL.getText());
        System.out.println("user: " + userId.getText());
        System.out.println("password: " + password.getText());
        // 更新DbUtils的配置
        DbUtils.url = connectionURL.getText();
        DbUtils.user = userId.getText();
        DbUtils.password = password.getText();


        // 示例：表名demo_test, 那么Domain = DemoTest
        String Domain = domainObjectName.getText();
        // domain = demoTest
        String domain = Domain.substring(0, 1).toLowerCase() + Domain.substring(1);
        // do_main = demo_test
        String do_main = domainObjectName.getText().replace("_", "-");
        // 表的中文名
        String tableNameCn = DbUtils.getTableComment(tableName.getText());
        List<Field> fieldList = DbUtils.getColumnByTableName(tableName.getText());
        Set<String> typeSet = getJavaTypes(fieldList);

        // System.out.println("列信息：" + JSONUtil.toJsonPrettyStr(fieldList));


        // 组装参数
        Map<String, Object> params = new HashMap<>();
        params.put("module", module);
        params.put("Domain", Domain);
        params.put("domain", domain);
        params.put("do_main", do_main);
        params.put("tableNameCn", tableNameCn);
        params.put("typeSet", typeSet);
        params.put("fieldList", fieldList);
        System.out.println("组装参数: " + params);

        gen(Domain, params, "service", "service");
        gen(Domain, params, "controller", "controller");
        gen(Domain, params, "req", "saveReq");
        gen(Domain, params,  "req","queryReq");
        gen(Domain, params,  "resp","queryResp");

    }


    /**
     * 生成指定模板的Java文件。
     * @param Domain 生成文件中使用的域名。
     * @param param 用于模板生成的参数集合。
     * @param packageName 目标Java文件所在的包名。
     * @param target 目标文件的基础名称。
     * @throws Exception 如果生成过程中发生错误，则抛出异常。
     */
    private static void gen(String Domain, Map<String, Object> param, String packageName, String target) throws IOException, TemplateException {
        // 初始化FreeMarker模板配置
        FreeMakerUtil.initConfig(target + ".ftl");
        // 构建目标文件路径
        String toPath = servicePath + packageName + "/";
        // 创建目标文件夹
        new File(toPath).mkdirs();
        // 根据目标文件名生成Java类名
        String Target = target.substring(0, 1).toUpperCase() + target.substring(1);
        // 完整的文件名
        String fileName = toPath + Domain + Target + ".java";
        // 输出开始生成的提示信息
        System.out.println("开始生成：" + fileName);
        // 使用模板生成Java文件
        FreeMakerUtil.generator(fileName, param);
    }


    /**
     * 获取生成器路径。
     * 该方法通过读取指定的XML文件，解析出其中的configurationFile节点的文本内容作为生成器路径。
     * @return 生成器路径字符串。
     * @throws DocumentException 如果读取或解析XML文件发生错误。
     */
    private static String getGeneratorPath() throws DocumentException {
        // 创建SAXReader实例用于读取XML文档
        SAXReader saxReader = new SAXReader();
        // 定义命名空间的映射
        Map<String, String> map = new HashMap<String, String>();

        // 添加POM的命名空间映射
        map.put("pom", "http://maven.apache.org/POM/4.0.0");

        // 设置文档工厂的命名空间URI
        saxReader.getDocumentFactory().setXPathNamespaceURIs(map);

        // 读取XML文件并将其转换为Document对象
        Document document = saxReader.read(pomPath);

        // 使用XPath表达式选择特定节点
        Node node = document.selectSingleNode("//pom:configurationFile");

        // 输出选择节点的文本内容
        System.out.println(node.getText());

        // 返回节点的文本内容
        return node.getText();
    }

    /**
     * 获取所有java类型，使用set去重
     * 遍历字段列表，提取每个字段的Java类型，并通过Set集合去重，返回一个不重复的Java类型集合。
     * @param fieldList 包含字段信息的列表，每个字段是一个Field对象。
     * @return 返回一个Set集合，包含不重复的Java类型字符串。
     */
    public static Set<String> getJavaTypes(List<Field> fieldList) {
        // 使用HashSet存储去重后的Java类型
        Set<String> set = new HashSet<>();
        // 遍历字段列表，获取每个字段的Java类型并添加到set中
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            set.add(field.getJavaType());
        }
        return set;
    }

}
