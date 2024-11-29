package pk.ztp.filmbase.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentDTO {
    @NotBlank(message = "Comment can't be empty")
    private String comment;

    @NotNull(message = "Comment must be associate with film by id")
    private Long filmId;
}
