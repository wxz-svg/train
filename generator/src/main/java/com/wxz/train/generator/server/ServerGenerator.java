package com.wxz.train.generator.server;

import com.wxz.train.generator.utils.FreeMakerUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务器代码生成器，利用FreeMarker模板生成指定名称的Java类。
 */
public class ServerGenerator {

    // 目标文件路径，生成的Java文件将保存在此路径下
    static String servicePath = "[module]/src/main/java/com//wxz//train//[module]/service/";

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


        // 示例：表名demo_test, 那么Domain = DemoTest
        String Domain = domainObjectName.getText();
        // domain = demoTest
        String domain = Domain.substring(0, 1).toLowerCase() + Domain.substring(1);
        // do_main = demo_test
        String do_main = domainObjectName.getText().replace("_", "-");

        // 组装参数
        Map<String, Object> params = new HashMap<>();
        params.put("Domain", Domain);
        params.put("domain", domain);
        params.put("do_main", do_main);
        System.out.println("组装参数: " + params);

        // 初始化模块配置，准备生成代码
        FreeMakerUtil.initConfig("service.ftl");
        // 根据模板和参数生成java文件
        FreeMakerUtil.generator(servicePath + Domain + "Service.java", params);
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
}
