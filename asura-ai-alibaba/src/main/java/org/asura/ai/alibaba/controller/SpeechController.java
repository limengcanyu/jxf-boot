
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

    @PostMapping("/tts")
    public ResponseEntity<TextToSpeechResponse> textToSpeech(@Valid @RequestBody TextToSpeechRequest request) {
        log.info("Received text to speech request");
        TextToSpeechResponse response = speechService.textToSpeech(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/stt")
    public ResponseEntity<SpeechToTextResponse> speechToText(@Valid @RequestBody SpeechToTextRequest request) {
        log.info("Received speech to text request");
        SpeechToTextResponse response = speechService.speechToText(request);
        return ResponseEntity.ok(response);
    }
}