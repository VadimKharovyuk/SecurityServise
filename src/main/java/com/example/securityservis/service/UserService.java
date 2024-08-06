package com.example.securityservis.service;

import com.example.securityservis.dto.EmailRequest;
import com.example.securityservis.dto.UserDTO;
import com.example.securityservis.model.User;
import com.example.securityservis.repository.EmailFeignClient;
import com.example.securityservis.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    @Getter
    private final PasswordEncoder passwordEncoder;
    private final EmailFeignClient emailFeignClient;


    public List<User> findAll() {
        return userRepository.findAll();
    }


    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Установите роль по умолчанию
        user.setRole(User.Role.USER);
        User newUser = userRepository.save(user);


        // Отправка email-письма через FeignClient
        EmailRequest emailRequest = new EmailRequest(newUser.getEmail(), "Welcome!", "Dear " + newUser.getUsername() + ", welcome to our store!");
        try {
            emailFeignClient.sendEmail(emailRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newUser;
    }


    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    @Transactional
    public boolean changePassword(String username, String currentPassword, String newPassword) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return false;
        }

        // Проверка текущего пароля
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return false;
        }

        // Шифрование и установка нового пароля
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return true;
    }

    @Transactional
    public void blockUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        optionalUser.ifPresent(user -> {
            user.setBlocked(true);
            userRepository.save(user);
        });
    }


    @Transactional
    public void unblockUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        optionalUser.ifPresent(user -> {
            user.setBlocked(false);
            userRepository.save(user); // Убедитесь, что сохранение происходит внутри транзакции
        });
    }

    public boolean isBlocked(String username) {
        User user = findByUsername(username);
        return user != null && user.isBlocked();
    }



    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword)); // Хэширование пароля
            userRepository.save(user);
        }
    }


    public UserDTO findByUsernameDto(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setRole(user.getRole().name());
            userDTO.setPassword(user.getPassword());
            return userDTO;
        }
        return null;
    }
    public BCryptPasswordEncoder getPasswordEncoder() {
        return (BCryptPasswordEncoder) passwordEncoder;
    }



    public ResponseEntity<Void> deleteUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Возвращаем статус 404 Not Found, если пользователь не найден
        }
    }
}


