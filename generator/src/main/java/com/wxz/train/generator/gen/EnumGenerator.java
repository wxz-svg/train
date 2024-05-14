package com.wxz.train.generator.gen;

import cn.hutool.core.util.StrUtil;
import com.wxz.train.business.enums.TrainTypeEnum;
import com.wxz.train.member.enums.PassengerTypeEnum;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;

public class EnumGenerator {
    //static String path = "web/src/assets/js/enums.js";
    static String path = "admin/src/assets/js/enums.js";

    public static void main(String[] args) {
        // 初始化用于存储转换结果的StringBuffer对象
        StringBuffer bufferObject = new StringBuffer();
        StringBuffer bufferArray = new StringBuffer();
        // 记录程序开始执行的时间
        long begin = System.currentTimeMillis();
        try {
            // 将PassengerTypeEnum类转换为JSON格式，分别存储在bufferObject和bufferArray中
            toJson(PassengerTypeEnum.class, bufferObject, bufferArray);
            // 将TrainTypeEnum类转换为JSON格式，分别存储在bufferObject和bufferArray中
            toJson(TrainTypeEnum.class, bufferObject, bufferArray);

            // 将两个StringBuffer对象的内容合并，并存储在buffer中
            StringBuffer buffer = bufferObject.append("\r\n").append(bufferArray);
            // 将合并后的内容写入到指定的js文件中
            writeJs(buffer);
        } catch (Exception e) {
            // 捕获并打印异常信息
            e.printStackTrace();
        }
        // 计算程序执行的总时间，并打印出来
        long end = System.currentTimeMillis();
        System.out.println("执行耗时:" + (end - begin) + " 毫秒");
    }


    /**
     * 将指定的枚举类转换成JSON格式的字符串。
     * @param clazz 需要转换的枚举类的Class对象。
     * @param bufferObject 用于存放转换后的枚举对象的JSON格式字符串。
     * @param bufferArray 用于存放转换后的枚举对象数组的JSON格式字符串。
     * @throws Exception 如果访问枚举类的方法或属性时发生错误，则抛出异常。
     */
    private static void toJson(Class clazz, StringBuffer bufferObject, StringBuffer bufferArray) throws Exception {
        // 将枚举类名称转换为下划线风格并去除非枚举部分
        String enumConst = StrUtil.toUnderlineCase(clazz.getSimpleName())
                .toUpperCase().replace("_ENUM", "");
        // 获取枚举常量数组
        Object[] objects = clazz.getEnumConstants();
        // 获取枚举常量的名称、描述和代码的方法
        Method name = clazz.getMethod("name");
        Method getDesc = clazz.getMethod("getDesc");
        Method getCode = clazz.getMethod("getCode");

        // 生成枚举对象的JSON字符串
        bufferObject.append(enumConst).append("={");
        for (int i = 0; i < objects.length; i++) {
            Object obj = objects[i];
            bufferObject.append(name.invoke(obj)).append(":{code:\"").append(getCode.invoke(obj)).append("\", desc:\"").append(getDesc.invoke(obj)).append("\"}");
            if (i < objects.length - 1) {
                bufferObject.append(",");
            }
        }
        bufferObject.append("};\r\n");

        // 生成枚举对象数组的JSON字符串
        bufferArray.append(enumConst).append("_ARRAY=[");
        for (int i = 0; i < objects.length; i++) {
            Object obj = objects[i];
            bufferArray.append("{code:\"").append(getCode.invoke(obj)).append("\", desc:\"").append(getDesc.invoke(obj)).append("\"}");
            if (i < objects.length - 1) {
                bufferArray.append(",");
            }
        }
        bufferArray.append("];\r\n");
    }

    /**
     * 将字符串缓冲区中的内容写入到文件中。
     * 注意：此方法不适用于频繁写入操作，因为它在每次调用时都会创建文件流，这可能导致性能问题。
     *
     * @param stringBuffer 要写入文件的字符串缓冲区。
     */
    public static void writeJs(StringBuffer stringBuffer) {
        FileOutputStream out = null;
        try {
            // 初始化文件输出流，准备写入文件
            out = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8"); // 使用UTF-8编码格式
            System.out.println(path); // 打印文件路径，用于调试确认
            osw.write(stringBuffer.toString()); // 将字符串缓冲区的内容写入文件
            osw.close(); // 关闭输出流
        } catch (Exception e) {
            e.printStackTrace(); // 捕获并打印异常信息
        }
        finally {
            try {
                // 确保输出流被正确关闭
                out.close();
            } catch (Exception e) {
                e.printStackTrace(); // 捕获并打印关闭输出流时的异常信息
            }

        }
    }


}