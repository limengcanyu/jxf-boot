package com.spring.boot.template.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/template")
@RestController
public class TemplateController {

    @RequestMapping("/getRequestParam")
    public Map<String, Object> getRequestParam(@RequestParam Integer id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("tenant_id", "tenant_000001");
        map.put("company_id", "company_000001");
        map.put("company_name", "公司000001");
        return map;
    }

    @RequestMapping("/getRequestParamBody")
    public Map<String, Object> getRequestParamBody(@RequestParam Integer id, @RequestBody Map<String, Object> body) {
        body.put("id", id);
        return body;
    }
}
