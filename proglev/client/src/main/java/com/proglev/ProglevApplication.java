package com.proglev;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.proglev.gui.ProgLevGui;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class ProglevApplication {

    @Bean
    public ObjectMapper objectMapper() {
        return new Jackson2ObjectMapperBuilder()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ProglevApplication.class, args);
		ProgLevGui.main(args);
    }
}
