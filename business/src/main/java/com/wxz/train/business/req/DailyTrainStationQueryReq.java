package com.wxz.train.business.req;

import com.wxz.train.common.req.PageReq;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class DailyTrainStationQueryReq extends PageReq {

    // 日期
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    // 车次编号
    private String trainCode;

}