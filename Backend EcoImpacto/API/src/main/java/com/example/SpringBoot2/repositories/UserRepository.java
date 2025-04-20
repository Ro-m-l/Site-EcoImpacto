package com.example.SpringBoot2.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.SpringBoot2.models.UserModel;

public interface UserRepository extends JpaRepository<UserModel, UUID> {

    Optional<UserModel> findByEmail(String email); // Adiciona o método para buscar por e-mail
}
