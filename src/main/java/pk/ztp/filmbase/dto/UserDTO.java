package pk.ztp.filmbase.dto;

import lombok.Builder;
import lombok.Data;
import pk.ztp.filmbase.model.User;

@Data
@Builder
public class UserDTO {

    private Long id;
    private String username;

    public static UserDTO from(User user) {
        return builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
