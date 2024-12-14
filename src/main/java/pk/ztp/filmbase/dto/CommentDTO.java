package pk.ztp.filmbase.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    @NotBlank(message = "Comment can't be empty")
    private String comment;

    @NotNull(message = "Comment must be associate with film by id")
    private Long filmId;
    private UserDTO user;
}
