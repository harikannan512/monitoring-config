package com.example.demoone.configuration;

import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricConfiguration {

    MeterRegistryCustomizer metricsCommonTags() {
        return registry -> registry.config().commonTags("environment", "dev", "team", "teamName");
    }
}
