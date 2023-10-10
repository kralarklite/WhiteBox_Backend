package org.ltboys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("org.ltboys.*.mapper")
@SpringBootApplication
public class WhiteBoxApplication {
    public static void main(String[] args) {
        System.out.println("====================SpringBootApplicationStart====================");
        SpringApplication.run(WhiteBoxApplication.class, args);
        System.out.println("====================SpringBootApplicationEnd====================");
    }
}