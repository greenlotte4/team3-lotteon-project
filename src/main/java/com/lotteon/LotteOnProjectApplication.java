package com.lotteon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.lotteon")
public class LotteOnProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(LotteOnProjectApplication.class, args);
    }

}
