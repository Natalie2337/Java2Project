package com.example.java2project;

import com.example.java2project.Service.DataCollectionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Java2ProjectApplication {

    private static final boolean initData = true;
    private static final boolean initBug = true;
    public static void main(String[] args) {
        SpringApplication.run(Java2ProjectApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(DataCollectionService service){
        return args -> {
            if (!initData) {
                service.collectData("java");
            }
            if (!initBug) {
                service.collectBug("java;exception");
            }
        };
    }
}
