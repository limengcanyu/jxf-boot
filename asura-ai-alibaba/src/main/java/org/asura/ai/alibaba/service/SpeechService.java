
package org.asura.ai.alibaba.service;

import org.asura.ai.alibaba.dto.request.SpeechToTextRequest;
import org.asura.ai.alibaba.dto.request.TextToSpeechRequest;
import org.asura.ai.alibaba.dto.response.SpeechToTextResponse;
import org.asura.ai.alibaba.dto.response.TextToSpeechResponse;

/**
 * 语音服务接口
 * 提供文本转语音和语音转文本功能
 */
public interface SpeechService {

    /**
     * 文本转语音
     * 
     * @param request 文本转语音请求
     * @return 语音合成响应
     */
    TextToSpeechResponse textToSpeech(TextToSpeechRequest request);

    /**
     * 语音转文本
     * 
     * @param request 语音转文本请求
     * @return 语音识别响应
     */
    SpeechToTextResponse speechToText(SpeechToTextRequest request);
}