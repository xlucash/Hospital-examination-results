package me.lukaszpisarczyk.Hospital.controllers;


import me.lukaszpisarczyk.Hospital.models.User;
import me.lukaszpisarczyk.Hospital.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('PATIENT') or hasRole('USER')")
    public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping("/pesel/{pesel}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<User> getUserByPesel(@PathVariable("pesel") String pesel) {
        return ResponseEntity.ok(userService.findUserByPesel(pesel));
    }

    @GetMapping("/current")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('PATIENT') or hasRole('USER')")
    public ResponseEntity<User> getUserFromToken() {
        return ResponseEntity.ok(userService.retrieveUserFromToken());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('PATIENT') or hasRole('USER')")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<?> getAllPatients() {
    	return ResponseEntity.ok(userService.getAllPatients());
    }
}
