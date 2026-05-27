
package org.asura.ai.alibaba.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asura.ai.alibaba.dto.request.ImageRequest;
import org.asura.ai.alibaba.dto.response.ImageResponse;
import org.asura.ai.alibaba.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/ai/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/generate")
    public ResponseEntity<ImageResponse> generateImage(@Valid @RequestBody ImageRequest request) {
        log.info("Received image generation request: {}", request.getPrompt());
        ImageResponse response = imageService.generateImage(request);
        return ResponseEntity.ok(response);
    }
}