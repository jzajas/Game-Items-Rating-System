package com.jzajas.RatingSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration.class
})
public class RatingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RatingSystemApplication.class, args);
    }

}
