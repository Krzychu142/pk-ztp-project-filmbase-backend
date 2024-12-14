package pk.ztp.filmbase.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pk.ztp.filmbase.enums.Genre;
import pk.ztp.filmbase.exception.ResourceNotFound;
import pk.ztp.filmbase.model.Film;
import pk.ztp.filmbase.repository.FilmRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmServiceTests {

    @Autowired
    private IFilmService filmService;

    @Autowired
    private FilmRepository filmRepository;

    @AfterEach
    public void tearDown() {
        filmRepository.deleteAll();
    }

    @Test
    void shouldReturnFilm() {
        // Arrange
        Film film = new Film();
        film.setId(1L);
        film.setTitle("test film");
        film.setGenre(Genre.ACTION);
        filmRepository.save(film);

        // Act
        Film result = filmService.getFilmById(film.getId());

        // Assert
        assertNotNull(result, "The result should not be null");
        assertEquals(film.getId(), result.getId(), "The IDs should match");
        assertEquals(film.getTitle(), result.getTitle(), "The titles should match");
        assertEquals(film.getGenre(), result.getGenre(), "The genres should match");
    }

    @Test
    void shouldThrowAnResourceNotFoundException() {
        // Arrange
        Long nonExistentId = 1L;

        // Act & Assert
        ResourceNotFound exception = assertThrows(
                ResourceNotFound.class,
                () -> filmService.getFilmById(nonExistentId),
                "Expected ResourceNotFound to be thrown"
        );

        // Additional assertion to check exception message
        assertEquals(
                "No film found with id: " + nonExistentId,
                exception.getMessage(),
                "The exception message should match"
        );
    }
}