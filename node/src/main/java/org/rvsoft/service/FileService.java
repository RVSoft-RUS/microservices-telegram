package org.rvsoft.service;

import org.rvsoft.entity.AppDocument;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    AppDocument processDoc(Message externalMessage);
}
