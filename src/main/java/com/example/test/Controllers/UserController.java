package com.example.test.Controllers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.test.Entities.IUserRepo;
import com.example.test.Entities.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepo userRepository;

    @PostMapping
    public ResponseEntity createUser(@RequestBody UserModel userModel){
        var user=this.userRepository.findByUsername(userModel.getUsername());
        if (user!=null) {
            System.out.println("Usuário já existente");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário Já existe");
        }
        var passwordHashed = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(passwordHashed);
        var userCreated = this.userRepository.save(userModel);


        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }
}
