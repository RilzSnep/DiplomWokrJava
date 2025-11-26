package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

public interface UserService {
    User getCurrentUser();
    UpdateUser updateUser(UpdateUser updateUser);
    void updatePassword(NewPassword newPassword);
    void updateUserImage(MultipartFile image);
}