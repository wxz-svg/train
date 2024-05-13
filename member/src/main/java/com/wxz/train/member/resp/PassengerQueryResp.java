package com.wxz.train.member.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

/**
 * 客户端查询乘客信息响应类
 * 用于封装乘客的详细信息，并提供给前端使用
 */
@Data
public class PassengerQueryResp {
    // 使用ToStringSerializer序列化id和memberId，确保传输过程中Long类型不会被误转为double
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id; // 乘客的唯一标识符

    @JsonSerialize(using= ToStringSerializer.class)
    private Long memberId; // 会员的唯一标识符

    private String name; // 乘客姓名

    private String idCard; // 乘客身份证号码

    private String type; // 乘客类型

    // 创建时间和更新时间格式化处理，确保前端接收到的时间格式一致
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime; // 记录的创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime; // 记录的最后更新时间
}
