package com.spring.boot.okhttp.service;

/**
 * <p>
 *
 * </p>
 *
 * @author rock.jxf
 * @date 2020/11/22 16:43
 */
public interface ResourceService {
    String getResourceFromResourceUtils();
    String getResourceFromClassPathResource();
    String getResourceFromFileSystemResource();
}
