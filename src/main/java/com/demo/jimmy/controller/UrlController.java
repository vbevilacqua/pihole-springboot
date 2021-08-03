package com.demo.jimmy.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.demo.jimmy.model.Request;
import com.demo.jimmy.model.Result;
import com.demo.jimmy.model.ViewUrl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UrlController {

    private RestTemplate rest;

    public UrlController(RestTemplate rest){
        this.rest = rest;
    }

    @GetMapping
    public ResponseEntity<String> getUrls() throws JsonProcessingException {
        var urls = new ArrayList<String>();
        for(int i = 1; i < 2519; i++) {
            Request request = getRequest(i);
            urls.addAll(processRequest(request));
        }

        if(urls.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            AtomicLong id = new AtomicLong(0);
            var result = urls.stream().parallel()
                .map(url -> new Result(id.getAndIncrement() , url))
                .collect(Collectors.toList());

            var mapper = new ObjectMapper();
            return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
        }
    }

    private Request getRequest(int i) {
        String url = "https://filterlists.com/api/directory/lists/" + i;
        Request request = new Request();
        try {
            log.info("Request to {}", url);
            request = rest.getForObject(url, Request.class);
        } catch(RestClientException ex) {
            request = null;
        }
        return request;
    }

    private List<String> processRequest(Request request) {
        var urls = new ArrayList<String>();
        if(request != null){
            urls.addAll(
                request
                    .getViewUrls()
                    .stream()
                    .map(ViewUrl::getUrl)
                    .filter(s -> s.contains("http") || s.contains("https"))
                    .collect(Collectors.toList())
                );
        }
        return Collections.unmodifiableList(urls);
    }

}