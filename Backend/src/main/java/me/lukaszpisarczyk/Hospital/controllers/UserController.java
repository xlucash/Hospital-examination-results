package me.lukaszpisarczyk.Hospital.controllers;


import me.lukaszpisarczyk.Hospital.models.User;
import me.lukaszpisarczyk.Hospital.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping("/pesel/{pesel}")
    public ResponseEntity<User> getUserByPesel(@PathVariable("pesel") String pesel) {
        return ResponseEntity.ok(userService.findUserByPesel(pesel));
    }
}
