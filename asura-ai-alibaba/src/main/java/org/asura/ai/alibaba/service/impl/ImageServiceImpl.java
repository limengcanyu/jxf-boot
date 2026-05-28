
package org.asura.ai.alibaba.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.asura.ai.alibaba.dto.request.ImageRequest;
import org.asura.ai.alibaba.dto.response.ImageResponse;
import org.asura.ai.alibaba.service.ImageService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    /**
     * 图片生成实现
     * 返回空图片列表作为占位符
     * 
     * @param request 图片生成请求
     * @return 图片生成响应
     */
    @Override
    public ImageResponse generateImage(ImageRequest request) {
        log.info("Generating image with prompt: {}", request.getPrompt());

        return ImageResponse.builder()
                .images(Collections.emptyList())
                .prompt(request.getPrompt())
                .resolution(request.getResolution())
                .build();
    }
}