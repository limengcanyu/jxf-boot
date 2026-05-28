
package org.asura.ai.alibaba.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asura.ai.alibaba.dto.request.SpeechToTextRequest;
import org.asura.ai.alibaba.dto.request.TextToSpeechRequest;
import org.asura.ai.alibaba.dto.response.SpeechToTextResponse;
import org.asura.ai.alibaba.dto.response.TextToSpeechResponse;
import org.asura.ai.alibaba.service.SpeechService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/ai/speech")
@RequiredArgsConstructor
public class SpeechController {

    private final SpeechService speechService;

    /**
     * 文本转语音接口
     * 
     * @param request 文本转语音请求对象，包含待转换的文本、语音格式、音色等参数
     * @return 返回语音合成结果，包含Base64编码的音频数据
     */
    @PostMapping("/tts")
    public ResponseEntity<TextToSpeechResponse> textToSpeech(@Valid @RequestBody TextToSpeechRequest request) {
        log.info("Received text to speech request");
        TextToSpeechResponse response = speechService.textToSpeech(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 语音转文本接口
     * 
     * @param request 语音转文本请求对象，包含音频数据、语言等参数
     * @return 返回语音识别结果，包含识别的文本内容和分段信息
     */
    @PostMapping("/stt")
    public ResponseEntity<SpeechToTextResponse> speechToText(@Valid @RequestBody SpeechToTextRequest request) {
        log.info("Received speech to text request");
        SpeechToTextResponse response = speechService.speechToText(request);
        return ResponseEntity.ok(response);
    }
}