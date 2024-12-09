package com.example.tracking.controllers;

import com.example.tracking.services.AsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.concurrent.CompletableFuture;

@RestController
public class TrackingWebServerController {
    private static final Logger logger = LoggerFactory.getLogger(TrackingWebServerController.class);

    @Autowired
    private AsyncService asyncService;

    @GetMapping("/ping")
    public CompletableFuture<ResponseEntity<String>> ping() {
        logger.info("Handling /ping request in thread: " + Thread.currentThread().getName());
        return asyncService.checkFilePresence().thenApply(response -> {
            logger.debug("Async result: " + response);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
            if ("OK".contentEquals(response)) {
                return new ResponseEntity<>(response, headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response, headers, HttpStatus.SERVICE_UNAVAILABLE);
            }
        });
    }

    @GetMapping("/img")
    public CompletableFuture<ResponseEntity<?>> img() {
        logger.info("Handling /img request in thread: " + Thread.currentThread().getName());

        return asyncService.loadImageAsync("static/1x1.gif")
                .thenApply(image -> {
                    if (image == null) {
                        logger.error("Image request failed: 1x1.gif not found");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                    }

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.IMAGE_GIF);
                    logger.info("Image request successful");

                    return new ResponseEntity<>(image, headers, HttpStatus.OK);
                })
                .exceptionally(e -> {
                    logger.error("Image request failed: " + e.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
                });
    }
}
