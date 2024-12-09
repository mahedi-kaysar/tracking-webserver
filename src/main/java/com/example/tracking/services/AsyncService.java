package com.example.tracking.services;

import com.example.tracking.controllers.TrackingWebServerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {
    private static final Logger logger = LoggerFactory.getLogger(AsyncService.class);

    @Async("taskExecutor")
    public CompletableFuture<String> checkFilePresence() {
        logger.info("Checking file presence in thread: " + Thread.currentThread().getName());
        File file = new File("/tmp/ok");
        if (file.exists()) {
            return CompletableFuture.completedFuture("OK");
        } else {
            logger.info("File not found, returning Service Unavailable");
            return CompletableFuture.completedFuture("Service Unavailable");
        }
    }

    @Async("taskExecutor")
    public CompletableFuture<Resource> loadImageAsync(String path) {
        logger.info("Loading image in thread: " + Thread.currentThread().getName());
        try {
            Resource image = new ClassPathResource(path);
            if (!image.exists()) {
                return CompletableFuture.completedFuture(null);
            }
            return CompletableFuture.completedFuture(image);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
