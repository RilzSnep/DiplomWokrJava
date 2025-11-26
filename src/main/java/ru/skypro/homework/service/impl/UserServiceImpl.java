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
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getCurrentUser() {
        UserEntity userEntity = getCurrentUserEntity();
        return userMapper.toDto(userEntity);
    }

    private UserEntity getCurrentUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
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
        if (!passwordEncoder.matches(newPassword.getCurrentPassword(), userEntity.getPassword())){
            throw new RuntimeException("Current password is incorrect");
        }
        userEntity.setPassword((passwordEncoder.encode(newPassword.getNewPassword())));
        userRepository.save(userEntity);

    }

    @Override
    public void updateUserImage(MultipartFile image) {
        log.info("Updating user image: {}", image.getOriginalFilename());
    }
}
