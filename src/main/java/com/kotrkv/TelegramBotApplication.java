package com.kotrkv;

import com.kotrkv.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootApplication
public class TelegramBotApplication {
    @Autowired
    private BotConfig botConfig;

    public static void main(String[] args) {
        SpringApplication.run(TelegramBotApplication.class, args);
    }

    @PostConstruct
    public void postConstruct() {
        log.info("Start...");
        log.info("BotConfig: {}", botConfig);
    }
}
