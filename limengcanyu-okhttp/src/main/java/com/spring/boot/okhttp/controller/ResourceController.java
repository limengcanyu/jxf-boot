package com.spring.boot.okhttp.controller;

import com.spring.boot.okhttp.service.ResourceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *
 * </p>
 *
 * @author rock.jxf
 * @date 2020/11/22 16:42
 */
@RestController
public class ResourceController {

    @Resource
    private ResourceService resourceService;

    /**
     * 可以读取jar包中文件
     *
     * https://localhost:9527/getResourceFromClassPathResource
     *
     * @return String
     */
    @RequestMapping("/getResourceFromClassPathResource")
    public String getResourceFromClassPathResource(){
        return resourceService.getResourceFromClassPathResource();
    }

    /**
     * 不能读取jar包中文件
     *
     * https://localhost:9527/getResourceFromResourceUtils
     *
     * @return String
     */
    @RequestMapping("/getResourceFromResourceUtils")
    public String getResourceFromResourceUtils(){
        return resourceService.getResourceFromResourceUtils();
    }

    /**
     * 可以读取jar包中文件
     *
     * https://localhost:9527/getResourceFromFileSystemResource
     *
     * @return String
     */
    @RequestMapping("/getResourceFromFileSystemResource")
    public String getResourceFromFileSystemResource(){
        return resourceService.getResourceFromFileSystemResource();
    }
}
