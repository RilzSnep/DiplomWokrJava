package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface ImageService {
    String saveImage(MultipartFile image) throws IOException;
    byte[] getImage(String id);
    void deleteImage(String id);
}