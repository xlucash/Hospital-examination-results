package me.lukaszpisarczyk.Hospital.controllers;


import me.lukaszpisarczyk.Hospital.models.User;
import me.lukaszpisarczyk.Hospital.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping("/pesel/{pesel}")
    public ResponseEntity<User> getUserByPesel(@PathVariable String pesel) {
        return ResponseEntity.ok(userService.findUserByPesel(pesel));
    }
}
