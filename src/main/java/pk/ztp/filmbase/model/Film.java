package pk.ztp.filmbase.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pk.ztp.filmbase.enums.Genre;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    private String director;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate releaseDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Genre genre;

    // Relation to Comment <- creatorUsername, content
    // Relation to Grade   <- creatorUsername, grade (int above 1 to 10)
    // maybe Average       <- sum of all grade / count of grade

}