package pk.ztp.filmbase.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import pk.ztp.filmbase.dto.JwtUserDTO;

import java.util.List;

@Component
public class JwtToUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {
    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt source) {
        JwtUserDTO jwtUser = new JwtUserDTO(source.getSubject());
        return new UsernamePasswordAuthenticationToken(jwtUser, source, List.of());
    }
}
