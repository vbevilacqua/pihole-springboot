package com.demo.jimmy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.List;

import com.demo.jimmy.controller.UrlController;
import com.demo.jimmy.model.Request;
import com.demo.jimmy.model.Result;
import com.demo.jimmy.model.ViewUrl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class UrlControllerTest {
    
    @Autowired
    private UrlController controller;

    @MockBean
    private RestTemplate mock;

    @Test
    @DisplayName("Should request URL and get all of the requests")
    void test1() throws JsonProcessingException {
        Mockito
            .when(mock.getForObject(anyString(), any()))
            .thenReturn(mockRequest());

        var result = controller.getUrls();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.hasBody());
        var content = serializeResult(result.getBody());
        assertTrue(content.get(0).getId() > -1);
        assertEquals(content.get(0).getAddress(), "https://280blocker.net/files/280blocker_adblock.txt");
    }

    @Test
    @DisplayName("Should process request even when list is empty")
    void test2() throws JsonProcessingException {
        Mockito
            .when(mock.getForObject(anyString(), any()))
            .thenThrow(RestClientException.class);

        var result = controller.getUrls();
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
    }

    private Request mockRequest(){
        return new Request(List.of(new ViewUrl("https://280blocker.net/files/280blocker_adblock.txt")));
    }

    private List<Result> serializeResult(String content) throws JsonMappingException, JsonProcessingException {
        var mapper = new ObjectMapper();
        return mapper.readValue(content, new TypeReference<List<Result>>(){} );
    }
}
