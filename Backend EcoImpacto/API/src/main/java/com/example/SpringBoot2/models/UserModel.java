package com.example.SpringBoot2.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "TB_USERS")
public class UserModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idUser;

    @Column(nullable = true)  // Permite valor nulo no banco de dados
    private String name;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail deve ser válido")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private int pontosMinigame;

    @Column(nullable = true)
    private int pontosQuiz;

    // Getters e Setters

    public UUID getIdUser() {
        return idUser;
    }

    public void setIdUser(UUID idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPontosMinigame() {
        return pontosMinigame;
    }

    public void setPontosMinigame(int pontosMinigame) {
        this.pontosMinigame = pontosMinigame;
    }

    public int getPontosQuiz() {
        return pontosQuiz;
    }

    public void setPontosQuiz(int pontosQuiz) {
        this.pontosQuiz = pontosQuiz;
    }
}