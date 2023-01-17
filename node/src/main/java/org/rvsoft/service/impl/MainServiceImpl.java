package org.rvsoft.service.impl;

import org.rvsoft.dao.AppUserDAO;
import org.rvsoft.dao.RawDataDao;
import org.rvsoft.entity.AppUser;
import org.rvsoft.entity.RawData;
import org.rvsoft.service.MainService;
import org.rvsoft.service.ProducerService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
public class MainServiceImpl implements MainService {
    private final RawDataDao rawDataDao;
    private final ProducerService producerService;
    private final AppUserDAO appUserDAO;

    public MainServiceImpl(
            RawDataDao rawDataDao,
            ProducerService producerService, AppUserDAO appUserDAO) {
        this.rawDataDao = rawDataDao;
        this.producerService = producerService;
        this.appUserDAO = appUserDAO;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);

        User telegramUser = update.getMessage().getFrom();
        AppUser appUser = findOrSaveAppUser(telegramUser);

        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Hello from NODE");

        producerService.produceAnswer(sendMessage);
    }

    private AppUser findOrSaveAppUser(User telegramUser) {
        AppUser persistentUser = appUserDAO.findAppUserByTelegramUserId(telegramUser.getId());
        if (persistentUser == null) {
            AppUser transientUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .userName(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    //todo change after registering
                    .isActive(true)
                    .build();
            return appUserDAO.save(transientUser);
        }
        return persistentUser;
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();
        rawDataDao.save(rawData);
    }
}
