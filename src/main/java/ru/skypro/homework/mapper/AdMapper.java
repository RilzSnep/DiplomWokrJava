package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;

@Component
public class AdMapper {

    public Ad toDto(AdEntity entity) {
        Ad dto = new Ad();
        dto.setPk(entity.getId());
        dto.setAuthor(entity.getAuthor().getId());
        dto.setImage(entity.getImage());
        dto.setPrice(entity.getPrice());
        dto.setTitle(entity.getTitle());
        return dto;
    }

    public ExtendedAd toExtendedAd(AdEntity entity) {
        ExtendedAd dto = new ExtendedAd();
        dto.setPk(entity.getId());
        dto.setAuthorFirstName(entity.getAuthor().getFirstName());
        dto.setAuthorLastName(entity.getAuthor().getLastName());
        dto.setDescription(entity.getDescription());
        dto.setEmail(entity.getAuthor().getEmail());
        dto.setImage(entity.getImage());
        dto.setPhone(entity.getAuthor().getPhone());
        dto.setPrice(entity.getPrice());
        dto.setTitle(entity.getTitle());
        return dto;
    }

    public AdEntity toEntity(CreateOrUpdateAd dto) {
        AdEntity entity = new AdEntity();
        entity.setTitle(dto.getTitle());
        entity.setPrice(dto.getPrice());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    public void updateEntityFromDto(CreateOrUpdateAd dto, AdEntity entity) {
        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle());
        }
        if (dto.getPrice() != null) {
            entity.setPrice(dto.getPrice());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
    }
}