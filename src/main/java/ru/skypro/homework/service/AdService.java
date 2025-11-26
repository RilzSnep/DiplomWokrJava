package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import java.util.List;

public interface AdService {
    Ads getAllAds();
    Ad createAd(CreateOrUpdateAd properties, MultipartFile image, Authentication authentication);
    ExtendedAd getAdById(Integer id);
    Ad updateAd(Integer id, CreateOrUpdateAd updateAd, Authentication authentication);
    void deleteAd(Integer id, Authentication authentication);
    Ads getCurrentUserAds(Authentication authentication);
    byte[] updateAdImage(Integer id, MultipartFile image, Authentication authentication);
    boolean isAdOwnerOrAdmin(Integer adId, Authentication authentication);
}