package pk.ztp.filmbase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JwtUserDTO {
    private String username;
    //List of roles if needed
}
