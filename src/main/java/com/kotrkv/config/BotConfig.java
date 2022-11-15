package com.kotrkv.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("bot.properties")
public class BotConfig {
    @Value("${bot.username}")
    private String botUserName;
    @Value("${bot.token}")
    private String botToken;
}
