package org.rvsoft.service.impl;

import org.rvsoft.dao.RawDataDao;
import org.rvsoft.entity.RawData;
import org.rvsoft.service.MainService;
import org.rvsoft.service.ProducerService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class MainServiceImpl implements MainService {
//    private final RawDataDao rawDataDao;
    private final ProducerService producerService;

    public MainServiceImpl(
//            RawDataDao rawDataDao,
            ProducerService producerService) {
//        this.rawDataDao = rawDataDao;
        this.producerService = producerService;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);

        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Hello from NODE");

        producerService.produceAnswer(sendMessage);
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();
//        rawDataDao.save(rawData);
    }
}
