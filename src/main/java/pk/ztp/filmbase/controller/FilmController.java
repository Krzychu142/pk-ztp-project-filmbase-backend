package pk.ztp.filmbase.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pk.ztp.filmbase.dto.ApiResponseDTO;
import pk.ztp.filmbase.enums.Genre;
import pk.ztp.filmbase.service.IFilmService;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final IFilmService filmService;

    @GetMapping("/film/{filmId}")
    public ResponseEntity<ApiResponseDTO> getFilmById(@Min(1) @PathVariable long filmId) {
        return ResponseEntity.ok().body(new ApiResponseDTO("ok", filmService.getFilmById(filmId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO> getAllFilms(
            @RequestParam(name = "page-number", defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(name = "page-size", defaultValue = "5") @Min(1) @Max(15) int pageSize,
            @RequestParam(name = "sort-direction", defaultValue = "ASC") @Pattern(regexp = "ASC|DESC", message = "Sort direction must be 'ASC' or 'DESC'.") String sortDirection,
            @RequestParam(name = "genre", required = false) Genre genre
    ) {
        return ResponseEntity.ok().body(new ApiResponseDTO("ok", filmService.getFilms(pageNumber, pageSize, sortDirection, genre)));
    }

}