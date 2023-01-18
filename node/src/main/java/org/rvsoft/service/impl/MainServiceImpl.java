package org.rvsoft.service.impl;

import lombok.extern.log4j.Log4j;
import org.rvsoft.dao.AppUserDAO;
import org.rvsoft.dao.RawDataDao;
import org.rvsoft.entity.AppDocument;
import org.rvsoft.entity.AppUser;
import org.rvsoft.entity.RawData;
import org.rvsoft.entity.enums.UserState;
import org.rvsoft.exeptions.UploadFileException;
import org.rvsoft.service.FileService;
import org.rvsoft.service.MainService;
import org.rvsoft.service.ProducerService;
import org.rvsoft.service.enums.ServiceCommand;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.rvsoft.entity.enums.UserState.BASIC_STATE;
import static org.rvsoft.entity.enums.UserState.WAIT_FOR_EMAIL_STATE;
import static org.rvsoft.service.enums.ServiceCommand.*;

@Log4j
@Service
public class MainServiceImpl implements MainService {
    private final RawDataDao rawDataDao;
    private final ProducerService producerService;
    private final AppUserDAO appUserDAO;
    private final FileService fileService;

    public MainServiceImpl(
            RawDataDao rawDataDao,
            ProducerService producerService, AppUserDAO appUserDAO, FileService fileService) {
        this.rawDataDao = rawDataDao;
        this.producerService = producerService;
        this.appUserDAO = appUserDAO;
        this.fileService = fileService;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);

        AppUser appUser = findOrSaveAppUser(update);
        UserState userState = appUser.getState();
        String text = update.getMessage().getText();
        String output = "";

        ServiceCommand serviceCommand = ServiceCommand.fromValue(text);
        if (CANCEL.equals(serviceCommand)) {
            output = cancelProcess(appUser);
        } else if (BASIC_STATE.equals(userState)) {
            output = processServiceCommand(appUser, text);
        } else if (WAIT_FOR_EMAIL_STATE.equals(userState)) {
            //TODO добавить обработку email
        } else {
            log.error("Unknown user state");
            output = "Неизвестная ошибка! Введите /cancel и попробуйте снова!";
        }

        Long chatId = update.getMessage().getChatId();
        sendAnswer(output, chatId);
    }

    @Override
    public void processDocMessage(Update update) {
        saveRawData(update);

        AppUser appUser = findOrSaveAppUser(update);
        Long chatId = update.getMessage().getChatId();

        if (isNotAllowToSendContent(chatId, appUser)) {
            return;
        }

        try {
            AppDocument doc = fileService.processDoc(update.getMessage());
            //TODO добавить сохр. документов
            String answer = "Документ успешно загружен!\n" +
                    "Ссылка жля скачивания: http://test.ru/get-doc/777";
            sendAnswer(answer, chatId);
        } catch (UploadFileException ex) {
            log.error(ex);
            String error = "К сожалению загрузка не удалась. Повторите попытку позже.";
            sendAnswer(error, chatId);
        }

    }

    @Override
    public void processPhotoMessage(Update update) {
        saveRawData(update);

        AppUser appUser = findOrSaveAppUser(update);
        Long chatId = update.getMessage().getChatId();

        if (isNotAllowToSendContent(chatId, appUser)) {
            return;
        }

        //TODO добавить сохр. фото
        String answer = "Фото успешно загружено!\n" +
                "Ссылка жля скачивания: http://test.ru/get-photo/778";
        sendAnswer(answer, chatId);
    }

    private boolean isNotAllowToSendContent(Long chatId, AppUser appUser) {
        UserState userState = appUser.getState();
        if (!appUser.getIsActive()) {
            String error = "Зарегистрируйтесь или активируйте свою учетную зпрись для загрузки контента.";
            sendAnswer(error, chatId);
            return true;
        } else if (!BASIC_STATE.equals(userState)) {
            String error = "Отмените текущую команду с помощью /cancel для отправки файлов.";
            sendAnswer(error, chatId);
            return true;
        }
        return false;
    }

    private void sendAnswer(String output, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);

        producerService.produceAnswer(sendMessage);
    }

    private String processServiceCommand(AppUser appUser, String cmd) {
        ServiceCommand serviceCommand = ServiceCommand.fromValue(cmd);
        if (REGISTRATION.equals(serviceCommand)) {
            //TODO добавить регистрацию
            return "Временно недоступно.";
        } else if (HELP.equals(serviceCommand)) {
            return help();
        } else if (START.equals(serviceCommand)) {
            return "Приветствую! Чтобы посмотреть список доступных команд, введите /help";
        } else {
            return "Неизвестная команда! Чтобы посмотреть список доступных команд, введите /help";
        }
    }

    private String help() {
        return "Список доступных команд:\n"
                + "/cancel - отмена выполнения текущей команды;\n"
                + "/registration - регистрация пользователя";
    }

    private String cancelProcess(AppUser appUser) {
        appUser.setState(BASIC_STATE);
        appUserDAO.save(appUser);
        return "Команда отменена!";
    }

    private AppUser findOrSaveAppUser(Update update) {
        User telegramUser = update.getMessage().getFrom();
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
