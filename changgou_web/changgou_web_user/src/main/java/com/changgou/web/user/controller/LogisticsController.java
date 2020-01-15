package com.changgou.web.user.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.common.entity.R;
import com.changgou.common.entity.Result;
import com.changgou.user.pojo.Trace;
import com.changgou.user.pojo.TraceVo;
import com.changgou.web.user.config.KdniaoTrackQueryAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author LiXiang
 */
@Controller
@RequestMapping("/logistics")
public class LogisticsController {

    @Autowired
    KdniaoTrackQueryAPI kdniaoTrackQueryAPI;

    @GetMapping
    @ResponseBody
    public Result getMessage(@RequestParam("orderId")String orderId) {
        Map map = null;
        TraceVo traceVo = null;
        try {
            String json = kdniaoTrackQueryAPI.getOrderTracesByJson("SF", orderId);
            map = JSON.parseObject(json, Map.class);
            traceVo = new TraceVo();
            traceVo.setShipperCode("顺丰");
            traceVo.setOrderId((String) map.get("LogisticCode"));
            List<Trace> tracesList = (List<Trace>) map.get("Traces");
            traceVo.setTraceList(tracesList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return R.T("查询",traceVo);
    }

}
