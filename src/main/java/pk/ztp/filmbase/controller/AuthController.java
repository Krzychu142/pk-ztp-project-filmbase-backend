package pk.ztp.filmbase.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pk.ztp.filmbase.dto.SignupDTO;
import pk.ztp.filmbase.model.User;
import pk.ztp.filmbase.security.TokenGenerator;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserDetailsManager userDetailsManager;
    private final TokenGenerator tokenGenerator;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignupDTO signupDTO) {
        User user = new User(signupDTO.getUsername(), signupDTO.getPassword());
        userDetailsManager.createUser(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(user, signupDTO.getPassword(), List.of());
        return ResponseEntity.ok(tokenGenerator.createToken(auth));
    }
}
