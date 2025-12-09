package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.Role;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    @Override
    public User getCurrentUser() {
        UserEntity userEntity = getCurrentUserEntity();
        return userMapper.toDto(userEntity);
    }

    @Override
    public User getUserById(Integer id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(userEntity);
    }

    @Override
    public User getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(userEntity);
    }

    @Override
    public UpdateUser updateUser(UpdateUser updateUser) {
        UserEntity userEntity = getCurrentUserEntity();
        userMapper.updateEntityFromDto(updateUser, userEntity);
        userRepository.save(userEntity);
        return updateUser;
    }

    @Override
    public void updatePassword(NewPassword newPassword) {
        UserEntity userEntity = getCurrentUserEntity();

        if (!passwordEncoder.matches(newPassword.getCurrentPassword(), userEntity.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        userEntity.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
        userRepository.save(userEntity);
    }

    @Override
    public void updateUserImage(MultipartFile image) {
        try {
            UserEntity userEntity = getCurrentUserEntity();

            // Сохраняем новое изображение
            String newImageId = imageService.saveImage(image);
            // Добавляем timestamp для предотвращения кэширования
            String timestamp = String.valueOf(System.currentTimeMillis());
            String newImagePath = "/images/" + newImageId + "?v=" + timestamp;

            // Удаляем старое изображение, если оно существует
            String oldImagePath = userEntity.getImage();
            if (oldImagePath != null && oldImagePath.startsWith("/images/")) {
                String oldImageId = oldImagePath.substring("/images/".length());
                // Убираем параметры если есть
                if (oldImageId.contains("?")) {
                    oldImageId = oldImageId.substring(0, oldImageId.indexOf("?"));
                }
                imageService.deleteImage(oldImageId);
            }

            // Обновляем путь к изображению у пользователя
            userEntity.setImage(newImagePath);
            userRepository.save(userEntity);

            log.info("User image updated: {}", newImagePath);
        } catch (IOException e) {
            log.error("Failed to save image", e);
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }
    }

    @Override
    public boolean isUserOwnerOrAdmin(Integer userId, Authentication authentication) {
        String currentUsername = authentication.getName();
        UserEntity currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return currentUser.getId().equals(userId) || currentUser.getRole() == Role.ADMIN;
    }

    private UserEntity getCurrentUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}