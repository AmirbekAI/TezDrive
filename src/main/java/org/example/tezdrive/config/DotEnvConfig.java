package org.example.tezdrive.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(
        value = "file:.env",
        ignoreResourceNotFound = true,
        factory = DotEnvPropertySourceFactory.class
)
public class DotEnvConfig {
}
