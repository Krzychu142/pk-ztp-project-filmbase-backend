package pk.ztp.filmbase.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import pk.ztp.filmbase.dto.TokenDTO;
import pk.ztp.filmbase.model.User;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Component
public class TokenGenerator {
    private final JwtEncoder accessTokenEncoder;
    private final JwtEncoder refreshTokenEncoder;

    public TokenGenerator(JwtEncoder accessTokenEncoder, @Qualifier("jwtRefreshTokenEncoder") JwtEncoder refreshTokenEncoder) {
        this.accessTokenEncoder = accessTokenEncoder;
        this.refreshTokenEncoder = refreshTokenEncoder;
    }

    public TokenDTO createToken(Authentication auth) {
        if (!(auth.getPrincipal() instanceof User user)) {
            throw new BadCredentialsException(
                    String.format("principal %s is not of type User", auth.getPrincipal().getClass())
            );
        }

        TokenDTO tokenDTO = new TokenDTO();
        // TODO: on refresh user do not have id bcs it's JwtDTO (with no id), eventually it's TODO
        tokenDTO.setUserId(user.getId());
        tokenDTO.setAccessToken(createAccessToken(auth));

        String refreshToken;
        if (auth.getCredentials() instanceof Jwt jwt) {
            Instant expiresAt = jwt.getExpiresAt();
            Duration duration = Duration.between(Instant.now(), expiresAt);
            long daysUntilExpired = duration.toDays();
            if (daysUntilExpired <= 7) {
                refreshToken = createRefreshToken(auth);
            } else {
                refreshToken = jwt.getTokenValue();
            }
        } else {
            refreshToken = createRefreshToken(auth);
        }
        tokenDTO.setRefreshToken(refreshToken);

        return tokenDTO;
    }

    private String createAccessToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("filmbase")
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.MINUTES))
                .id(UUID.randomUUID().toString())
                .subject(user.getUsername())
                .build();
        return accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    private String createRefreshToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("filmbase")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.DAYS))
                .id(UUID.randomUUID().toString())
                .subject(user.getUsername())
                .build();
        return refreshTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

}
