package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    public String saveImage(MultipartFile image) throws IOException {
        String id = UUID.randomUUID().toString();

        ImageEntity imageEntity = new ImageEntity(
                id,
                image.getBytes(),  // byte[] напрямую
                image.getContentType()
        );

        imageRepository.save(imageEntity);
        log.info("Image saved with id: {}, size: {} bytes", id, image.getSize());
        return id;
    }


    @Override
    public byte[] getImage(String id) {
        ImageEntity imageEntity = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found with id: " + id));
        return imageEntity.getImage();
    }

    @Override
    public void deleteImage(String id) {
        if (imageRepository.existsById(id)) {
            imageRepository.deleteById(id);
            log.info("Image deleted with id: {}", id);
        }
    }
}