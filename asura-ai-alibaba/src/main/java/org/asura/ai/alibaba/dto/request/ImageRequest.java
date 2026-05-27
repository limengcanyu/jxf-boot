
package org.asura.ai.alibaba.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageRequest {

    @NotBlank(message = "图片描述不能为空")
    private String prompt;

    private String style = "写实风格";

    private String resolution = "1024x1024";

    private Integer n = 1;
}