package pk.ztp.filmbase.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RateRequestDTO {
    @NotNull(message = "Rate must be associate with film by id")
    private Long filmId;

    @NotNull(message = "Grade is required.")
    @Min(value = 1, message = "Grade must be at least {value}.")
    @Max(value = 5, message = "Grade must be at most {value}.")
    private Integer grade;
}
