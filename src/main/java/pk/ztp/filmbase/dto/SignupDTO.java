package pk.ztp.filmbase.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupDTO {
    @NotBlank(message = "Username cannot be blank.")
    private String username;
    @NotBlank(message = "Password cannot be blank.")
    @Min(value = 6, message = "Password must be at least 6 characters long.")
    private String password;
}
