package org.rvsoft.service.impl;

import lombok.extern.log4j.Log4j;
import org.rvsoft.service.ConsumerService;
import org.rvsoft.service.MainService;
import org.rvsoft.service.ProducerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.rvsoft.model.RabbitQueue.*;

@Service
@Log4j
public class ConsumerServiceImpl implements ConsumerService {
    private final MainService mainService;

    public ConsumerServiceImpl(ProducerService producerService, MainService mainService) {
        this.mainService = mainService;
    }

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumeTextMessageUpdate(Update update) {
        log.debug("NODE: Text message is received");
        mainService.processTextMessage(update);

    }

    @Override
    @RabbitListener(queues = DOC_MESSAGE_UPDATE)
    public void consumeDocMessageUpdate(Update update) {
        log.debug("NODE: Doc message is received");
    }

    @Override
    @RabbitListener(queues = PHOTO_MESSAGE_UPDATE)
    public void consumePhotoMessageUpdate(Update update) {
        log.debug("NODE: Photo message is received");
    }
}
