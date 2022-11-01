package org.rvsoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class DispatcherApplication {
    public static void main(String[] args) {
        SpringApplication.run(DispatcherApplication.class);
    }
}
