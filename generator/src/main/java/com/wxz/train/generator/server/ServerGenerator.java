package com.wxz.train.generator.server;

import freemarker.template.TemplateException;
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
    static String toPath = "generator\\src\\main\\java\\com\\wxz\\train\\generator\\test\\";

    static String pomPath = "generator\\pom.xml";

    // 在类加载时创建目标路径下的文件夹，确保路径存在
    static {
        new File(toPath).mkdirs();
    }

    /**
     * 程序入口点。
     * 主要功能是使用SAXReader解析XML文件，定位并输出特定节点的文本内容。
     * @param args 命令行参数，当前方法未使用此参数。
     * @throws Exception 可能抛出的异常，包括TemplateException和其他IO异常。
     * @throws TemplateException 当处理模板时发生错误。
     */
    public static void main(String[] args) throws Exception {
        // 获取生成器路径
        String generatorPath = getGeneratorPath();

        // 使用SAXReader读取并解析XML文件
        Document document = new SAXReader().read("generator/" + generatorPath);

        // 定位到table节点
        Node table = document.selectSingleNode("//table");
        System.out.println(table);

        // 获取table节点的tableName和domainObjectName属性值
        Node tableName = table.selectSingleNode("@tableName");
        Node domainObjectName = table.selectSingleNode("@domainObjectName");

        // 输出属性值
        System.out.println(tableName.getText() + "/" + domainObjectName.getText());
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
