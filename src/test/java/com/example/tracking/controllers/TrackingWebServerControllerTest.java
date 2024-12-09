package com.example.tracking.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.concurrent.CompletableFuture;

import com.example.tracking.services.AsyncService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(TrackingWebServerController.class)
public class TrackingWebServerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AsyncService asyncService;

    @Test
    public void testPingFileExists() throws Exception {
        when(asyncService.checkFilePresence()).thenReturn(CompletableFuture.completedFuture("OK"));

        MvcResult mvcResult = mockMvc.perform(get("/ping"))
                .andExpect(request().asyncStarted())
                .andDo(print())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().string("OK"));
    }
    @Test
    public void testPingFileNotExists() throws Exception {
        when(asyncService.checkFilePresence()).thenReturn(CompletableFuture.completedFuture("Service Unavailable"));

        MvcResult mvcResult = mockMvc.perform(get("/ping"))
                .andExpect(request().asyncStarted())
                .andDo(print())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Service Unavailable"));
    }

    @Test
    public void testImgFileExists() throws Exception {
        Resource image = new ClassPathResource("static/1x1.gif");
        when(asyncService.loadImageAsync("static/1x1.gif")).thenReturn(CompletableFuture.completedFuture(image));

        MvcResult mvcResult = mockMvc.perform(get("/img"))
                .andExpect(request().asyncStarted())
                .andDo(print())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_GIF));
    }

    @Test
    public void testImgFileNotExists() throws Exception {
        when(asyncService.loadImageAsync("static/1x1.gif")).thenReturn(CompletableFuture.completedFuture(null));

        MvcResult mvcResult = mockMvc.perform(get("/img"))
                .andExpect(request().asyncStarted())
                .andDo(print())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testImgFileLoadError() throws Exception {
        when(asyncService.loadImageAsync("static/1x1.gif")).thenReturn(CompletableFuture.failedFuture(new Exception("Error loading image")));

        MvcResult mvcResult = mockMvc.perform(get("/img"))
                .andExpect(request().asyncStarted())
                .andDo(print())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isInternalServerError());
    }
}