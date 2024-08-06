package com.example.securityservis.controller;

import com.example.securityservis.dto.UserDTO;
import com.example.securityservis.model.User;
import com.example.securityservis.repository.UserRepository;
import com.example.securityservis.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;



    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

@PostMapping("/register")
public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
    User user = new User();
    user.setUsername(userDTO.getUsername());
    user.setEmail(userDTO.getEmail());
    user.setPassword(userDTO.getPassword());
    User newUser = userService.registerUser(user);

    UserDTO newUserDTO = new UserDTO();
    newUserDTO.setId(newUser.getId());
    newUserDTO.setUsername(newUser.getUsername());
    newUserDTO.setEmail(newUser.getEmail());
    newUserDTO.setRole(newUser.getRole().name());

    return ResponseEntity.status(HttpStatus.CREATED).body(newUserDTO);
}

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestParam String username,
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {
        boolean success = userService.changePassword(username, currentPassword, newPassword);
        if (success) {
            return ResponseEntity.ok("Password changed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Current password is incorrect");
        }
    }

    @PostMapping("/block/{userId}")
    public ResponseEntity<String> blockUser(@PathVariable Long userId) {
        userService.blockUser(userId);
        return ResponseEntity.ok("User blocked successfully");
    }

    @PostMapping("/unblock/{userId}")
    public ResponseEntity<String> unblockUser(@PathVariable Long userId) {
        userService.unblockUser(userId);
        return ResponseEntity.ok("User unblocked successfully");
    }

    @GetMapping("/is-blocked")
    public ResponseEntity<Boolean> isBlocked(@RequestParam String username) {
        boolean blocked = userService.isBlocked(username);
        return ResponseEntity.ok(blocked);
    }

@PostMapping("/login")
public ResponseEntity<UserDTO> login(@RequestParam String username, @RequestParam String password) {
    // Проверка существования пользователя
    if (userService.isBlocked(username)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Возвращаем FORBIDDEN если заблокирован
    }

    UserDTO userDTO = userService.findByUsernameDto(username);
    if (userDTO == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    // Проверка совпадения пароля
    boolean passwordMatches = userService.getPasswordEncoder().matches(password, userDTO.getPassword());
    if (!passwordMatches) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    return ResponseEntity.ok(userDTO);
}

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        ResponseEntity<Void> response = userService.deleteUserById(id);
        return response; // Передаем ответ от сервиса напрямую
    }










}
