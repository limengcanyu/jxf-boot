
package org.asura.ai.alibaba.service;

import org.asura.ai.alibaba.dto.request.ImageRequest;
import org.asura.ai.alibaba.dto.response.ImageResponse;

/**
 * 图片生成服务接口
 */
public interface ImageService {

    /**
     * 生成图片
     * 
     * @param request 图片生成请求
     * @return 图片生成响应
     */
    ImageResponse generateImage(ImageRequest request);
}