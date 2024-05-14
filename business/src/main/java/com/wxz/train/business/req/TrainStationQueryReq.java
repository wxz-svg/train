package com.wxz.train.business.req;

import com.wxz.train.common.req.PageReq;
import lombok.Data;

@Data
public class TrainStationQueryReq extends PageReq {
    private String trainCode;
}