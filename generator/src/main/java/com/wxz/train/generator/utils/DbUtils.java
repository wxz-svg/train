package com.wxz.train.generator.utils;

import cn.hutool.core.util.StrUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DbUtils {

    // 静态变量，用于存储数据库的URL、用户名和密码
    public static String url = "";
    public static String user = "";
    public static String password = "";

    /**
     * 获取与数据库的连接。
     *
     * @return Connection 返回与数据库建立的连接对象。
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // 加载MySQL驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 使用DbUtils类中的静态变量作为连接参数
            String url = DbUtils.url;
            String user = DbUtils.user;
            String password = DbUtils.password;

            // 尝试建立数据库连接
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            // 处理驱动加载失败异常
            e.printStackTrace();
        } catch (SQLException e) {
            // 处理数据库连接失败异常
            e.printStackTrace();
        }
        return conn;
    }


    /**
     * 获得表注释
     * @param tableName 表名
     * @return 表的注释
     * @throws Exception 如果查询过程中出现错误，则抛出异常
     */
    public static String getTableComment(String tableName) throws Exception {
        // 获取数据库连接
        Connection conn = getConnection();
        // 创建Statement对象用于执行SQL查询
        Statement stmt = conn.createStatement();
        // 构造查询表注释的SQL语句，并执行查询
        ResultSet rs = stmt.executeQuery("select table_comment from information_schema.tables Where table_name = '" + tableName + "'");
        String tableNameCH = "";
        // 处理查询结果，获取表注释
        if (rs != null) {
            while(rs.next()) {
                tableNameCH = rs.getString("table_comment");
                break; // 只需要获取第一个匹配的表注释，因此查询到后即跳出循环
            }
        }
        // 关闭ResultSet
        rs.close();
        // 关闭Statement
        stmt.close();
        // 关闭Connection
        conn.close();
        // 打印获取的表名注释，通常用于调试或日志记录
        System.out.println("表名：" + tableNameCH);
        return tableNameCH; // 返回获取到的表注释
    }


    /**
     * 根据表名获取所有列的信息
     * @param tableName 表名
     * @return 返回包含所有列信息的列表
     * @throws Exception 如果过程中出现错误，则抛出异常
     */
    public static List<Field> getColumnByTableName(String tableName) throws Exception {
        // 初始化用于存储列信息的列表
        List<Field> fieldList = new ArrayList<>();
        // 获取数据库连接
        Connection conn = getConnection();
        // 创建Statement对象用于执行SQL语句
        Statement stmt = conn.createStatement();
        // 执行SQL查询语句，获取所有列的详细信息
        ResultSet rs = stmt.executeQuery("show full columns from `" + tableName + "`");
        if (rs != null) {
            // 遍历结果集，逐个提取列信息，并封装到Field对象中
            while(rs.next()) {
                // 提取列名、类型、注释等信息
                String columnName = rs.getString("Field");
                String type = rs.getString("Type");
                String comment = rs.getString("Comment");
                String nullAble = rs.getString("Null"); // 判断列是否允许为空

                // 根据提取的信息，创建并填充Field对象
                Field field = new Field();
                field.setName(columnName);
                field.setNameHump(lineToHump(columnName));
                field.setNameBigHump(lineToBigHump(columnName));
                field.setType(type);
                field.setJavaType(DbUtils.sqlTypeToJavaType(rs.getString("Type")));
                field.setComment(comment);
                // 如果注释中包含'|'，则提取注释的中文名
                if (comment.contains("|")) {
                    field.setNameCn(comment.substring(0, comment.indexOf("|")));
                } else {
                    field.setNameCn(comment);
                }
                // 判断列是否允许为空
                field.setNullAble("YES".equals(nullAble));
                // 如果列类型为varchar，提取长度信息
                if (type.toUpperCase().contains("varchar".toUpperCase())) {
                    String lengthStr = type.substring(type.indexOf("(") + 1, type.length() - 1);
                    field.setLength(Integer.valueOf(lengthStr));
                } else {
                    field.setLength(0);
                }
                // 如果注释中包含"枚举"，处理枚举相关的属性
                if (comment.contains("枚举")) {
                    field.setEnums(true);

                    // 从注释中提取枚举常量的名称，并设置到Field对象中
                    int start = comment.indexOf("[");
                    int end = comment.indexOf("]");
                    String enumsName = comment.substring(start + 1, end); // 提取枚举类名
                    String enumsConst = StrUtil.toUnderlineCase(enumsName)
                            .toUpperCase().replace("_ENUM", "");
                    field.setEnumsConst(enumsConst);
                } else {
                    field.setEnums(false);
                }
                // 将填充好的Field对象添加到列表中
                fieldList.add(field);
            }
        }
        // 关闭结果集、Statement和Connection
        rs.close();
        stmt.close();
        conn.close();
        // 打印列信息，用于调试
        System.out.println("列信息：" + fieldList);
        // 返回包含所有列信息的列表
        return fieldList;
    }


    /**
     * 将下划线分隔的字符串转换为小驼峰命名法的字符串。
     * 例如：member_id 转成 memberId
     * @param str 需要转换的字符串
     * @return 转换后的小驼峰命名法字符串
     */
    public static String lineToHump(String str){
        // 编译匹配下划线后跟一个字母的正则表达式
        Pattern linePattern = Pattern.compile("_(\\w)");
        str = str.toLowerCase(); // 将字符串转换为全小写
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        // 遍历匹配结果，将下划线后的字母转换为大写并替换原字符串
        while(matcher.find()){
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb); // 将剩余部分追加到结果字符串
        return sb.toString();
    }

    /**
     * 将下划线分隔的字符串转换为大驼峰命名法的字符串。
     * 例如：member_id 转成 MemberId
     * @param str 需要转换的字符串
     * @return 转换后的大驼峰命名法字符串
     */
    public static String lineToBigHump(String str){
        // 使用lineToHump转换为小驼峰命名，然后将首字母大写
        String s = lineToHump(str);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 将数据库类型转换为对应的Java类型。
     *
     * @param sqlType 数据库数据类型，例如：varchar、char、text、datetime、bigint、int、long、decimal、boolean等。
     * @return 对应的Java数据类型，例如：String、Date、Long、Integer、BigDecimal、Boolean等。
     */
    public static String sqlTypeToJavaType(String sqlType) {
        if (sqlType.toUpperCase().contains("varchar".toUpperCase())
                || sqlType.toUpperCase().contains("char".toUpperCase())
                || sqlType.toUpperCase().contains("text".toUpperCase())) {
            return "String";
        } else if (sqlType.toUpperCase().contains("datetime".toUpperCase())) {
            return "Date";
        } else if (sqlType.toUpperCase().contains("time".toUpperCase())) {
            return "Date";
        } else if (sqlType.toUpperCase().contains("date".toUpperCase())) {
            return "Date";
        } else if (sqlType.toUpperCase().contains("bigint".toUpperCase())) {
            return "Long";
        } else if (sqlType.toUpperCase().contains("int".toUpperCase())) {
            return "Integer";
        } else if (sqlType.toUpperCase().contains("long".toUpperCase())) {
            return "Long";
        } else if (sqlType.toUpperCase().contains("decimal".toUpperCase())) {
            return "BigDecimal";
        } else if (sqlType.toUpperCase().contains("boolean".toUpperCase())) {
            return "Boolean";
        } else {
            return "String";
        }
    }

}