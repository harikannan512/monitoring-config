package com.example.demoone.controller;

import com.example.demoone.service.ConsulUrlOneService;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("first")
public class FirstController {

    private final ConsulUrlOneService consulService;

    private final MeterRegistry meterRegistry;

    @Value("${spring.application.name}")
    private String springApplicationName;

    private Timer timer;

    private Counter counter;

    @Autowired
    public FirstController(MeterRegistry meterRegistry, ConsulUrlOneService consulService) {
        this.consulService = consulService;
        this.meterRegistry = meterRegistry;
        timer = meterRegistry.timer(springApplicationName+"_demoOneTimer","team","teamName","environment","dev");
        counter = Counter.builder("LoadCardCounter" )
                .tags()
                .register(meterRegistry);

    }

    @GetMapping
    public String returnFirst(){
        return "first";
    }

    @Timed(percentiles = {0.5, 0.95, 0.999}, histogram = true)
    @GetMapping("/post/{request}")
    public String processBatchFile(@PathVariable String request) {
        counter.increment();
        Map<String, String> map = new HashMap<>();
        Iterable<Measurement> mt = counter.measure();
        Iterator iter = mt.iterator();
        while (iter.hasNext()) {
            Measurement mm = (Measurement) iter.next();
            Statistic st = mm.getStatistic();
            map.put(st.name(), st.getTagValueRepresentation());
        }
        map.put("count", "" + counter.count());

        Gauge.builder("PayloadSize", () -> request.length())
                .tags("team", "teamName", "counter-statistics", "" + map.toString())
                .register(meterRegistry);


        timer.record(() -> {
            try {
                Thread.sleep((long) (Math.random() * 500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        return "String in request is: " + request;
    }

    @Timed(percentiles = {0.5, 0.95, 0.999}, histogram = true)
    @GetMapping("chain")
    public String returnFirstConcat(){
        timer.record(()->{
            try{
                Thread.sleep((long) (Math.random()*500));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        });
        String responseFromSecond = consulService.secondServiceRepsonse(consulService.serviceUrl());
        return String.format("first %s", responseFromSecond);
    }
}
