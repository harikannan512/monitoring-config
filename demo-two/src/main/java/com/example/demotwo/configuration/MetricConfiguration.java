package com.example.demotwo.configuration;

import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricConfiguration {

    @Bean
    MeterRegistryCustomizer meterRegistryCustomizer() {
        return registry -> registry.config().commonTags("environment", "dev", "team", "teamName");
    }
}
