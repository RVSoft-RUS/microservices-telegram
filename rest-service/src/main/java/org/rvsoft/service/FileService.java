package org.rvsoft.service;

import org.rvsoft.entity.AppDocument;
import org.rvsoft.entity.AppPhoto;
import org.rvsoft.entity.BinaryContent;
import org.springframework.core.io.FileSystemResource;

public interface FileService {
    AppDocument getDocument(String id);
    AppPhoto getPhoto(String id);
    FileSystemResource getFileSystemResource(BinaryContent binaryContent);
}
