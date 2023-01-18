package org.rvsoft.controller;

import org.rvsoft.entity.AppDocument;
import org.rvsoft.entity.AppPhoto;
import org.rvsoft.entity.BinaryContent;
import org.rvsoft.service.FileService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/file")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get-doc")
    public ResponseEntity<?> getDoc(@RequestParam("id") String id) {
        AppDocument doc = fileService.getDocument(id);
        if (isNull(doc)) {
            return ResponseEntity.badRequest().build();
        }

        BinaryContent binaryContent = doc.getBinaryContent();
        //Файл на диске = 0 байт
        FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);
        if (isNull(fileSystemResource)) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getMimeType()))
                .header("Content-disposition", "attachment; filename=" + doc.getDocName())
                .body(fileSystemResource);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get-photo")
    public ResponseEntity<?> getPhoto(@RequestParam("id") String id) {
        AppPhoto photo = fileService.getPhoto(id);
        if (isNull(photo)) {
            return ResponseEntity.badRequest().build();
        }

        BinaryContent binaryContent = photo.getBinaryContent();

        FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);
        if (isNull(fileSystemResource)) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header("Content-disposition", "attachment;")
                .body(fileSystemResource);
    }
}
