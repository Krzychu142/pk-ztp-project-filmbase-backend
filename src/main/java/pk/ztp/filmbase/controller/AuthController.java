package pk.ztp.filmbase.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pk.ztp.filmbase.dto.ApiResponseDTO;
import pk.ztp.filmbase.dto.LoginDTO;
import pk.ztp.filmbase.dto.SignupDTO;
import pk.ztp.filmbase.dto.TokenDTO;
import pk.ztp.filmbase.model.User;
import pk.ztp.filmbase.security.TokenGenerator;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserDetailsManager userDetailsManager;
    private final TokenGenerator tokenGenerator;
    private final DaoAuthenticationProvider authenticationProvider;
    @Qualifier("jwtRefreshAuthenticationProvider")
    private final JwtAuthenticationProvider jwtRefreshAuthenticationProvider;

    public AuthController (
            UserDetailsManager userDetailsManager,
            TokenGenerator tokenGenerator,
            DaoAuthenticationProvider authenticationProvider,
            JwtAuthenticationProvider jwtRefreshAuthenticationProvider
    ) {
        this.userDetailsManager = userDetailsManager;
        this.tokenGenerator = tokenGenerator;
        this.authenticationProvider = authenticationProvider;
        this.jwtRefreshAuthenticationProvider = jwtRefreshAuthenticationProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO> register(@Valid @RequestBody SignupDTO signupDTO) {
        User user = new User(signupDTO.getUsername(), signupDTO.getPassword());
        userDetailsManager.createUser(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(user, signupDTO.getPassword(), List.of());
        return ResponseEntity.ok().body(new ApiResponseDTO("Registered", tokenGenerator.createToken(auth)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        Authentication authRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginDTO.getUsername(), loginDTO.getPassword());
        Authentication authResult = authenticationProvider.authenticate(authRequest);
        return ResponseEntity.ok().body(new ApiResponseDTO("Logged", tokenGenerator.createToken(authResult)));
    }

    @PostMapping("/token")
    public ResponseEntity<ApiResponseDTO> token(@Valid @RequestBody TokenDTO tokenDTO) {
        Authentication auth = jwtRefreshAuthenticationProvider.authenticate(new BearerTokenAuthenticationToken(tokenDTO.getRefreshToken()));
        // Jwt jwt = (Jwt) auth.getPrincipal();
        // check if present in db and not revoked
        return ResponseEntity.ok().body(new ApiResponseDTO("Refreshed", tokenGenerator.createToken(auth)));
    }
}
