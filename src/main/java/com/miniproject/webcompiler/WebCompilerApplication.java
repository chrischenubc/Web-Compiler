package com.miniproject.webcompiler;

import com.miniproject.webcompiler.storage.StorageProperties;
import com.miniproject.webcompiler.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class WebCompilerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebCompilerApplication.class, args);
    }

    /**
     * Initialize and empty the temporary storage folder.
     *         Execute only once after the application starts.
     */
    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }

}
