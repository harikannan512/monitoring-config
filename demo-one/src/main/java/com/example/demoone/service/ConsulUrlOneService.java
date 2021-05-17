package com.example.demoone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ConsulUrlOneService {

    @Autowired
    @Qualifier(value = "remoteRestTemplate")
    private RestTemplate rest;

    @Autowired
    private DiscoveryClient discoveryClient;

//    @Value("${spring.application.name}")
//    private String applicationName;

    public String serviceUrl() {
        List<ServiceInstance> services = discoveryClient.getInstances("demo-two-spring");

        String protocol = null;
        String requestEndpoint = null;
        String host = services.get(0).getHost();
        int port = services.get(0).getPort();

        protocol = services.get(0).getMetadata().getOrDefault("protocol", "http");
        requestEndpoint = services.get(0).getMetadata().getOrDefault("endpointUrlTwo", "/second");
        String urlString = String.format("%s://%s:%d%s", protocol, host, port, requestEndpoint);
        return urlString;
    }

    public String secondServiceRepsonse(String urlString) {
        String endpoint = urlString;

        String response = rest.exchange(endpoint, HttpMethod.GET, null,
                new ParameterizedTypeReference<String>(){}).getBody();
        return response;
    }


    @Bean(name = "remoteRestTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}