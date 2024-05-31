package ru.mars.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
@Slf4j
public class FileServiceMock {


    public String saveFile(MultipartFile multipartFile) {
        return "";
    }

    public byte[] download(String fileId) {
        return null;
    }
}