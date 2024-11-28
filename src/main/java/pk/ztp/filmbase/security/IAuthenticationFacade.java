package pk.ztp.filmbase.security;

import pk.ztp.filmbase.model.User;

public interface IAuthenticationFacade {
    User getCurrentUser();
}
