package org.devlord.onehand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class OnehandbackendApplication {

    public static void main(String[] args) {
//        System.setProperty("spring.devtools.livereload.enabled","true");
//        System.setProperty("spring.devtools.restart.enabled", "true");
        SpringApplication.run(OnehandbackendApplication.class, args);
    }

}
