package pk.ztp.filmbase.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
    private Long userId;
    private String accessToken;
    @NotBlank
    private String refreshToken;
}
