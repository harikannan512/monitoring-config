package com.example.demotwo.controller;

import com.example.demotwo.service.ConsulOneService;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/second")
public class SecondController {

    private final ConsulOneService consulService;
    private final Timer timer;
    private final MeterRegistry registry;

    @Value("${spring.application.name}")
    private String springApplicationName;

    @Autowired
    public SecondController(MeterRegistry registry, ConsulOneService consulService) {
        this.consulService = consulService;
        this.registry = registry;
        timer = registry.timer(springApplicationName+"_demoTwoTimer","team","teamName","environment","dev");
    }

    @GetMapping
    public String returnSecond() {
        return "second";
    }

    @GetMapping("chain")
    @Timed(percentiles = {0.1, 0.5, 0.95}, histogram = true)
    public String returnSecondConcat() {
        timer.record(()->{
            try{
                Thread.sleep((long) (Math.random()*500));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        });
        String firstResponse = consulService.firstServiceResponse(consulService.serviceUrlOne());
        return String.format("second + response from first: %s", firstResponse);
    }
}
