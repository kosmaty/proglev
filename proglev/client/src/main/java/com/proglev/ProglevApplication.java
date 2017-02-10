package com.proglev;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.proglev.gui.ProgLevGui;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SpringBootApplication
public class ProglevApplication {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
//        return new Jackson2ObjectMapperBuilder()
//                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//                .build();
    }

    @Bean
    public Executor pregnancyRepositoryExecutor(){
        return Executors.newSingleThreadExecutor();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ProglevApplication.class, args);
		ProgLevGui.main(args);
    }
}
