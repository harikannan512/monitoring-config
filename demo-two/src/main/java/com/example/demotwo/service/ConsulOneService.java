package com.example.demotwo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ConsulOneService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ConsulOneService.class);

    @Bean("remoteRestTemplate")
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Autowired
    @Qualifier("remoteRestTemplate")
    private RestTemplate rest;

    @Autowired
    private DiscoveryClient discoveryClient;

    public String serviceUrlOne() {
        List<ServiceInstance> services = discoveryClient.getInstances("demo-one-spring");

        String host = services.get(0).getHost();
        LOGGER.info("host from consul = {}", host);
        int port = services.get(0).getPort();

        String protocol = services.get(0).getMetadata().getOrDefault("protocol", "");
        String endpointUrl = services.get(0).getMetadata().getOrDefault("endpointUrlOne", "");

        String serviceUrl = String.format("%s://%s:%d%s", protocol, host, port, endpointUrl);

        return serviceUrl;
    }

    public String firstServiceResponse(String serviceUrl) {
        String endpoint = serviceUrl;
        String response = rest.exchange(endpoint, HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
        }).getBody();

        return response;
    }


}
