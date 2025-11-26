package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

public interface UserService {
    User getCurrentUser();
    User getUserById(Integer id);
    User getUserByEmail(String email);
    UpdateUser updateUser(UpdateUser updateUser);
    void updatePassword(NewPassword newPassword);
    void updateUserImage(MultipartFile image);
    boolean isUserOwnerOrAdmin(Integer userId, Authentication authentication);
}