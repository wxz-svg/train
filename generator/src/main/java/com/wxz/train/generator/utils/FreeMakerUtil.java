package com.wxz.train.generator.utils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FreeMakerUtil {

    // 路径:ftl模板存放位置
    static String ftlPath = "generator\\src\\main\\java\\com\\wxz\\train\\generator\\ftl\\";

    static Template template;

    /**
     * 初始化并加载模板配置。
     * 该方法用于设置FreeMarker模板的配置，并加载指定的模板文件。
     *
     * @param ftlName 模板文件的名称，不包含文件扩展名。此文件应位于配置中指定的目录下。
     * @throws Exception 如果在加载模板或配置模板过程中发生错误，则抛出异常。
     */
    public static void initConfig(String ftlName) throws IOException {
        // 创建并配置FreeMarker的Configuration实例
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setDirectoryForTemplateLoading(new File(ftlPath)); // 设置模板加载目录
        configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_31)); // 设置对象包装器

        // 加载指定名称的模板
        template = configuration.getTemplate(ftlName);
    }


    /**
     * 根据读取的模板，生成代码。
     *
     * @param fileName 需要生成的代码文件名称，包含文件路径。
     * @param map 包含代码生成所需数据的键值对集合。
     * @throws IOException 当文件读写发生错误时抛出。
     * @throws TemplateException 当处理模板发生错误时抛出。
     */
    public static void generator(String fileName, Map<String, Object> map) throws IOException, TemplateException{
        // 创建文件写入流并包装为缓冲写入流
        FileWriter fileWriter = new FileWriter(fileName);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        // 使用模板处理map中的数据，并写入到bufferedWriter中
        template.process(map, bufferedWriter);
        // 刷新bufferedWriter，确保所有数据都写入到文件中
        bufferedWriter.flush();
        // 关闭bufferedWriter
        bufferedWriter.close();
    }

}
