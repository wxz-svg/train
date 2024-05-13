package com.wxz.train.member.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wxz.train.common.req.PageReq;
import lombok.Data;

import java.util.Date;

@Data
public class PassengerQueryReq extends PageReq {

    private Long memberId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
