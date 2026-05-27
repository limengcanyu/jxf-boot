
package org.asura.ai.alibaba.service;

import org.asura.ai.alibaba.dto.request.ImageRequest;
import org.asura.ai.alibaba.dto.response.ImageResponse;

public interface ImageService {

    ImageResponse generateImage(ImageRequest request);
}