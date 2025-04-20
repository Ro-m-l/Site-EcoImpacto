package com.example.SpringBoot2.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRecordDto(
    @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
    String name,

    @NotBlank(message = "E-mail é obrigatório") 
    @Email(message = "E-mail deve ser válido") 
    String email,

    @NotBlank(message = "Senha é obrigatória") 
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres") 
    String password,

    // Não vai iniciar com pontos necessariamente
    int pontosMinigame,

    // Respostas de quiz
    int pontosQuiz
) {}