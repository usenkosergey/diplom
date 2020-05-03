package ru.skillbox.diplom.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class StorageConfig implements WebMvcConfigurer, CommandLineRunner {

    private String location = "upload";

    public String getLocation() {
        return location;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(location + "/*")
                .addResourceLocations("file:" + location + "/");
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            Files.createDirectories(Paths.get(location));
        } catch (IOException e) {
            System.out.println(e);
        };
    }
}
