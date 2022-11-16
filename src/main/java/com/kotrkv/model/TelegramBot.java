package com.kotrkv.model;

import com.kotrkv.config.BotConfig;
import com.kotrkv.model.entity.UserData;
import com.kotrkv.repository.UserRepository;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    private UserRepository userRepository;
    private BotConfig botConfig;
    private final String HELP_TEXT_MESSAGE = "This is help message";

    public TelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
        List<BotCommand> botCommands = new ArrayList<>();
        botCommands.add(new BotCommand("/start", "Начало работы с ботом"));
        botCommands.add(new BotCommand("/mydata", "Данные пользователя"));
        botCommands.add(new BotCommand("/help", "Справка о команде"));
        botCommands.add(new BotCommand("/settings", "Настройки"));
        try {
            this.execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Ошибка добавления списка команд: {}", e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUserName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            switch (message) {
                case "/start": {
                    registerUser(update.getMessage());
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                }
                case "/help": {
                    startCommandReceived(chatId, HELP_TEXT_MESSAGE);
                    break;
                }
                default:
                    sendMessage(chatId, "Sorry...");
            }
        }
    }

    private void registerUser(Message message) {
        Optional<UserData> byId = userRepository.findById(message.getChatId());
        if (byId.isEmpty()) {
            UserData userData = new UserData();
            userData.setChatId(message.getChatId());
            userData.setFirstName(message.getChat().getFirstName());
            userData.setLastName(message.getChat().getLastName());
            userData.setUserName(message.getChat().getUserName());
            userData.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
            userRepository.save(userData);
        }
    }

    private void startCommandReceived(Long chatId, String firstName) {
        String answer = EmojiParser.parseToUnicode("Hi, " + firstName + ", nice to meet you!" + " :blush:");
        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String message) {
        try {
            execute(SendMessage.builder()
                    .chatId(Long.toString(chatId))
                    .text(message)
                    .build());
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения: ", e.getMessage());
        }
    }
}
