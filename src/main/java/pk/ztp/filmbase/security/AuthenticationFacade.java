package pk.ztp.filmbase.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pk.ztp.filmbase.dto.JwtUserDTO;
import pk.ztp.filmbase.model.User;
import pk.ztp.filmbase.service.UserManager;

@Component
@RequiredArgsConstructor
public class AuthenticationFacade implements IAuthenticationFacade {
    private final UserManager userManager;

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof JwtUserDTO jwtUser){
                return (User) userManager.loadUserByUsername(jwtUser.getUsername());
            }
            if (authentication.getPrincipal() instanceof User){
                return (User) userManager.loadUserByUsername(((User) authentication.getPrincipal()).getUsername());
            }
        }
        throw new RuntimeException("No user is currently logged in");
    }
}
