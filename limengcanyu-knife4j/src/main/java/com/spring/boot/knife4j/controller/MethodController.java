package com.spring.boot.knife4j.controller;

import com.spring.boot.knife4j.common.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "多方法", tags = "相同URL")
@RequestMapping("/api/methods")
@RestController
public class MethodController {

    @ApiOperation(value = "相同url,不同method", notes = "针对相同url，不同mehtod类型未展示bug")
    @RequestMapping(value = "/allMethod")
    public RestMessage allMethod() {
        RestMessage r = new RestMessage();
        r.setData("针对相同url，不同mehtod类型未展示bug");
        return r;
    }

}
