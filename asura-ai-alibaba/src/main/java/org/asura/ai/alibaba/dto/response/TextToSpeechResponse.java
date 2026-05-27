
package org.asura.ai.alibaba.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextToSpeechResponse {

    private String audioBase64;

    private String format;

    private Integer duration;

    private String voice;
}