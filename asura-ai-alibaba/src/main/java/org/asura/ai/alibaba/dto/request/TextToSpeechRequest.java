
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
public class TextToSpeechRequest {

    @NotBlank(message = "文本内容不能为空")
    private String text;

    private String voice = "Aiyue";

    private Double rate = 1.0;

    private Double volume = 1.0;

    private String format = "mp3";
}