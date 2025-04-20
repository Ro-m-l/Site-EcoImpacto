package com.example.SpringBoot2.controllers;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.SpringBoot2.dtos.UserRecordDto;
import com.example.SpringBoot2.models.UserModel;
import com.example.SpringBoot2.repositories.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Método de registro (onde o nome é opcional)
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody @Valid UserRecordDto userRecordDto) {
        if (userRepository.findByEmail(userRecordDto.email()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("E-mail já está em uso.");
        }
        try {
            System.out.println("Nome recebido no DTO: " + userRecordDto.name()); // Log para verificar o valor do nome

            UserModel userModel = new UserModel();
            userModel.setEmail(userRecordDto.email());
            userModel.setName(userRecordDto.name() != null ? userRecordDto.name() : ""); // Define um valor padrão se o nome for nulo
            userModel.setPassword(passwordEncoder.encode(userRecordDto.password()));
            userRepository.save(userModel);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso.");
        } catch (Exception e) {
            e.printStackTrace(); // Log completo do erro
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar.");
        }
    }

    // Método de login (onde o nome não é necessário)
    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody @Valid UserRecordDto userRecordDto) {
        Optional<UserModel> user = userRepository.findByEmail(userRecordDto.email());
        if (user.isEmpty() || !passwordEncoder.matches(userRecordDto.password(), user.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha inválidos.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Login realizado com sucesso.");
    }

    @PostMapping("/pontosMinigame")
    public ResponseEntity<Void> pontosMinigame(@RequestBody @Valid UserRecordDto userRecordDto) {
        System.out.println("Recebido POST /pontosMinigame");
        System.out.println("Email recebido: " + userRecordDto.email());
        System.out.println("Pontos recebidos: " + userRecordDto.pontosMinigame());
        
        Optional<UserModel> userOptional = userRepository.findByEmail(userRecordDto.email());
        if (userOptional.isEmpty()) {
            System.out.println("Usuário não encontrado: " + userRecordDto.email());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    
        UserModel user = userOptional.get();
        if (!passwordEncoder.matches(userRecordDto.password(), user.getPassword())) {
            System.out.println("Senha incorreta para o usuário: " + userRecordDto.email());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    
        System.out.println("Usuário autenticado: " + user.getEmail());
        user.setPontosMinigame(userRecordDto.pontosMinigame());
        System.out.println("Pontuação a ser salva: " + user.getPontosMinigame());
    
        // Verificar se o método save está sendo chamado corretamente
        userRepository.save(user);
        System.out.println("Pontuação salva para o usuário: " + user.getEmail());
        
        return ResponseEntity.status(HttpStatus.OK).build();
    }    

    @PostMapping("/pontosQuiz")
    public ResponseEntity<Void> pontosQuiz(@RequestBody @Valid UserRecordDto userRecordDto) {
        System.out.println("Recebido POST /pontosQuiz");
        System.out.println("Email recebido: " + userRecordDto.email());
        System.out.println("Pontos recebidos: " + userRecordDto.pontosQuiz());
        
        Optional<UserModel> userOptional = userRepository.findByEmail(userRecordDto.email());
        if (userOptional.isEmpty()) {
            System.out.println("Usuário não encontrado: " + userRecordDto.email());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    
        UserModel user = userOptional.get();
        if (!passwordEncoder.matches(userRecordDto.password(), user.getPassword())) {
            System.out.println("Senha incorreta para o usuário: " + userRecordDto.email());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    
        System.out.println("Usuário autenticado: " + user.getEmail());
        user.setPontosQuiz(userRecordDto.pontosQuiz());
        System.out.println("Pontuação a ser salva: " + user.getPontosQuiz());
    
        userRepository.save(user);
        System.out.println("Pontuação salva para o usuário: " + user.getEmail());
        
        return ResponseEntity.status(HttpStatus.OK).build();
    }    

    // Método para salvar usuário (como no registro)
    @PostMapping("/users")
    public ResponseEntity<UserModel> saveUser(@RequestBody @Valid UserRecordDto userRecordDto) {
        var userModel = new UserModel();
        BeanUtils.copyProperties(userRecordDto, userModel);
        userModel.setPassword(passwordEncoder.encode(userRecordDto.password())); // Criptografa a senha
        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(userModel));
    }

    // Método para buscar todos os usuários
    @GetMapping("/users")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
    }

    // Método para buscar um usuário pelo ID
    @GetMapping("/users/{id}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "id") UUID id) {
        Optional<UserModel> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(user.get());
    }

    // Método para atualizar um usuário
    @PutMapping("/users/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "id") UUID id,
                                             @RequestBody @Valid UserRecordDto userRecordDto) {
        Optional<UserModel> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }
        var userModel = user.get();
        BeanUtils.copyProperties(userRecordDto, userModel);
        userModel.setPassword(passwordEncoder.encode(userRecordDto.password())); // Atualiza a senha criptografada
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(userModel));
    }

    // Método para deletar um usuário
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") UUID id) {
        Optional<UserModel> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }
        userRepository.delete(user.get());
        return ResponseEntity.status(HttpStatus.OK).body("Usuário deletado com sucesso.");
    }
}
