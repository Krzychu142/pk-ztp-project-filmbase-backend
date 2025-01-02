package pk.ztp.filmbase.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RateResponseDTO {
    private Long id;
    @NotBlank(message = "Grade is required.")
    private Integer grade;

    @NotNull(message = "Rate must be associate with film by id")
    private Long filmId;
    private UserDTO user;
}
