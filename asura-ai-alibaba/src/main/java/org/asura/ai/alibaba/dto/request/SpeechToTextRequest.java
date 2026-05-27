
package org.asura.ai.alibaba.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpeechToTextRequest {

    private String audioUrl;

    private String audioBase64;

    private String format = "mp3";

    private Boolean enablePunctuation = true;
}