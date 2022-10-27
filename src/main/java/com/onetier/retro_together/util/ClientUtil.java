package com.onetier.retro_together.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Component

public class ClientUtil {

    @Value("${github.url}")
    private String githubUrl;
    @Value("${github.token}")
    private String githubToken;



    public void  requestgit(String postName, String content) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // Header set
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("authorization", githubToken);
        httpHeaders.add("cache-control", "no-cache");
        httpHeaders.add("content-type", "application/json");
        httpHeaders.add("token_type", "bearer");

        // Body set
        Map<String, String> json = new HashMap<>();
        Map<String, String> json_value = new HashMap<>();
        json_value.put("name", "\"" + "Puri12" + "\"");
        json_value.put("email", "\"" + "tkdqja9812@gmpiail.com" + "\"");
        json.put("message", "\"" + postName + "\"");
        json.put("content", Base64.getUrlEncoder().encodeToString(content.getBytes()));
        json.put("commiter", json_value.toString());

        // Message
        HttpEntity<?> requestMessage = new HttpEntity<>(json, httpHeaders);


        // Request
        restTemplate.put(githubUrl + postName, requestMessage, String.class);
    }

}