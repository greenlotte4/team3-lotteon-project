package com.lotteon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.lotteon.repository")
public class LotteOnProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(LotteOnProjectApplication.class, args);
    }

}
