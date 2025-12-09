package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.Role;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;
    private final ImageService imageService;

    @Override
    public Ads getAllAds() {
        List<AdEntity> adEntities = adRepository.findAll();
        List<Ad> adDtos = adEntities.stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());

        Ads ads = new Ads();
        ads.setCount(adDtos.size());
        ads.setResults(adDtos);
        return ads;
    }

    @Override
    public Ad createAd(CreateOrUpdateAd properties, MultipartFile image, Authentication authentication) {
        try {
            UserEntity author = getUserFromAuthentication(authentication);

            // Сохраняем изображение
            String imageId = imageService.saveImage(image);
            String imagePath = "/images/" + imageId;

            AdEntity adEntity = adMapper.toEntity(properties);
            adEntity.setAuthor(author);
            adEntity.setImage(imagePath);

            AdEntity savedAd = adRepository.save(adEntity);
            log.info("Ad created with id: {}", savedAd.getId());
            return adMapper.toDto(savedAd);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    @Override
    public ExtendedAd getAdById(Integer id) {
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));
        return adMapper.toExtendedAd(adEntity);
    }

    @Override
    public Ad updateAd(Integer id, CreateOrUpdateAd updateAd, Authentication authentication) {
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        if (!isAdOwnerOrAdmin(id, authentication)) {
            throw new RuntimeException("Access denied");
        }

        adMapper.updateEntityFromDto(updateAd, adEntity);
        AdEntity updatedAd = adRepository.save(adEntity);
        return adMapper.toDto(updatedAd);
    }

    @Override
    public void deleteAd(Integer id, Authentication authentication) {
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        if (!isAdOwnerOrAdmin(id, authentication)) {
            throw new RuntimeException("Access denied");
        }

        // Удаляем изображение объявления
        String imagePath = adEntity.getImage();
        if (imagePath != null && imagePath.startsWith("/images/")) {
            String imageId = imagePath.substring("/images/".length());
            imageService.deleteImage(imageId);
        }

        adRepository.delete(adEntity);
        log.info("Ad deleted with id: {}", id);
    }

    @Override
    public Ads getCurrentUserAds(Authentication authentication) {
        UserEntity user = getUserFromAuthentication(authentication);
        List<AdEntity> userAds = adRepository.findByAuthor(user);

        List<Ad> adDtos = userAds.stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());

        Ads ads = new Ads();
        ads.setCount(adDtos.size());
        ads.setResults(adDtos);
        return ads;
    }

    @Override
    public byte[] updateAdImage(Integer id, MultipartFile image, Authentication authentication) {
        try {
            if (!isAdOwnerOrAdmin(id, authentication)) {
                throw new RuntimeException("Access denied");
            }

            AdEntity adEntity = adRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Ad not found"));

            // Удаляем старое изображение
            String oldImagePath = adEntity.getImage();
            if (oldImagePath != null && oldImagePath.startsWith("/images/")) {
                String oldImageId = oldImagePath.substring("/images/".length());
                imageService.deleteImage(oldImageId);
            }

            // Сохраняем новое изображение
            String newImageId = imageService.saveImage(image);
            String newImagePath = "/images/" + newImageId;

            // Обновляем объявление
            adEntity.setImage(newImagePath);
            adRepository.save(adEntity);

            log.info("Ad image updated for ad id: {}", id);
            return image.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to update image", e);
        }
    }

    @Override
    public boolean isAdOwnerOrAdmin(Integer adId, Authentication authentication) {
        AdEntity adEntity = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        String currentUsername = authentication.getName();
        UserEntity currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return adEntity.getAuthor().getId().equals(currentUser.getId()) ||
                currentUser.getRole() == Role.ADMIN;
    }

    private UserEntity getUserFromAuthentication(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}