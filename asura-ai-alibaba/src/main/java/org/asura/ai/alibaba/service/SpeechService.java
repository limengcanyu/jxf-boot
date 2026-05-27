
package org.asura.ai.alibaba.service;

import org.asura.ai.alibaba.dto.request.SpeechToTextRequest;
import org.asura.ai.alibaba.dto.request.TextToSpeechRequest;
import org.asura.ai.alibaba.dto.response.SpeechToTextResponse;
import org.asura.ai.alibaba.dto.response.TextToSpeechResponse;

public interface SpeechService {

    TextToSpeechResponse textToSpeech(TextToSpeechRequest request);

    SpeechToTextResponse speechToText(SpeechToTextRequest request);
}