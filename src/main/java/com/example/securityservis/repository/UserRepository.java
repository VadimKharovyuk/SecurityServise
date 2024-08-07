package com.example.securityservis.repository;

import com.example.securityservis.dto.UserDTO;
import com.example.securityservis.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);

   Optional<User> findByEmail(String email);



    boolean existsByEmail(String email);


}
