package com.spring.boot.knife4j.controller;

import com.google.common.collect.ImmutableMap;
import com.spring.boot.knife4j.common.ReqEntity;
import com.spring.boot.knife4j.common.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@Api(value = "测试", tags = "测试用例")
@RestController
@RequestMapping("/api/test")
public class TestController {

    @ApiOperation(value = "puttest", notes = "puttest测试说明2")
    @PutMapping(value = "/puttest")
    @ApiImplicitParam(value = "code", name = "code", dataType = "string", paramType = "query", defaultValue = "abc")
    public RestMessage puttest(String code) {
        return new RestMessage(code);
    }

    @ApiOperation(value = "deletetest", notes = "deletetest测试说明2")
    @DeleteMapping(value = "/deletetest")
    @ApiImplicitParam(value = "code", name = "code", dataType = "string", paramType = "query", example = "abcefg")
    public RestMessage deletetest(@RequestParam(value = "code") String code) {
        return new RestMessage(code);
    }

    @PostMapping("/reqbody")
    @ApiOperation(value = "RequestBody接口类型", notes = "RequestBody测试接口,实体类型")
    public RestMessage reqbody(@RequestBody ReqEntity reqEntity) {
        return new RestMessage(reqEntity);
    }

    @PostMapping("/reqbody2")
    @ApiOperation(value = "RequestBody接口类型2", notes = "RequestBody测试接口2-string类型")
    public RestMessage reqbody2(@RequestBody String reqEntity) {
        return new RestMessage(reqEntity);
    }

    @PostMapping("/reqbody1")
    @ApiOperation(value = "ModelAttribute", notes = "ModelAttribute类型参数")
    public RestMessage reqbody1(@ModelAttribute ReqEntity reqEntity) {
        return new RestMessage(reqEntity);
    }

    @PostMapping("/reqbody3")
    @ApiOperation(value = "header参数", notes = "header参数")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "code", name = "code", dataType = "string", paramType = "query"),
            @ApiImplicitParam(value = "headerparam", name = "headerparam", dataType = "string", paramType = "header"),
            @ApiImplicitParam(value = "page", name = "page", dataType = "int", paramType = "query"),
            @ApiImplicitParam(value = "page1", name = "page1", dataType = "Long", paramType = "query")
    })
    public RestMessage reqbody3(@RequestHeader(value = "headerparam") String headerparam, @RequestParam(value = "code") String code,
                                @RequestParam(value = "page") int page, @RequestParam(value = "page1") Long page1) {
        return new RestMessage(ImmutableMap.of("code", code, "header", headerparam, "page", page, "page1", page1));
    }
}
