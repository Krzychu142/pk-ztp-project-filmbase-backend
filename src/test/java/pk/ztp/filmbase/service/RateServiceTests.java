package pk.ztp.filmbase.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import pk.ztp.filmbase.dto.RateRequestDTO;
import pk.ztp.filmbase.dto.RateResponseDTO;
import pk.ztp.filmbase.enums.Genre;
import pk.ztp.filmbase.model.Film;
import pk.ztp.filmbase.model.Rate;
import pk.ztp.filmbase.model.User;
import pk.ztp.filmbase.repository.FilmRepository;
import pk.ztp.filmbase.repository.RateRepository;
import pk.ztp.filmbase.repository.UserRepository;

@SpringBootTest
public class RateServiceTests {

    @Autowired
    private IRateService rateService;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RateRepository rateRepository;

    @AfterEach
    public void tearDown() {
        filmRepository.deleteAll();
        userRepository.deleteAll();
        rateRepository.deleteAll();
    }

    @Test
    void shouldReturnRateResponseDTOWhenFilmExistsAndValidRateRequestDTO() {
        // Arrange
        final int grade = 3;

        User user = new User();
        user.setUsername("testuser");
        User savedUser = userRepository.save(user);

        Film film = new Film();
        film.setTitle("Test Film");
        film.setGenre(Genre.ACTION);
        Film savedFilm = filmRepository.save(film);

        RateRequestDTO rateRequestDTO = new RateRequestDTO();
        rateRequestDTO.setFilmId(savedFilm.getId());
        rateRequestDTO.setGrade(grade);

        //Act
        RateResponseDTO rateResponseDTO = rateService.rateFilm(rateRequestDTO, savedUser);

        //Assert
        Assertions.assertNotNull(rateResponseDTO);
        Assertions.assertEquals(grade, rateResponseDTO.getGrade());
        Assertions.assertEquals(savedFilm.getId(), rateResponseDTO.getFilmId());
    }

    @Test
    void shouldDeleteRateIfFilmExistsAndUserIsOwner(){
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        User savedUser = userRepository.save(user);

        Film film = new Film();
        film.setTitle("Test Film");
        film.setGenre(Genre.ACTION);
        Film savedFilm = filmRepository.save(film);

        Rate rate = new Rate();
        rate.setUser(savedUser);
        rate.setFilm(savedFilm);
        rate.setGrade(4);
        Rate savedRate = rateRepository.save(rate);

        //Act
        rateService.deleteRate(savedRate.getId(), savedUser);

        //Assert
        Assertions.assertTrue(rateRepository.findById(savedRate.getId()).isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwner() {
        User guest = new User();
        guest.setUsername("testuser");
        User savedGuest = userRepository.save(guest);

        User owner = new User();
        owner.setUsername("testuser2");
        User savedOwner = userRepository.save(owner);

        Film film = new Film();
        film.setTitle("Test Film");
        film.setGenre(Genre.ACTION);
        Film savedFilm = filmRepository.save(film);

        Rate rate = new Rate();
        rate.setUser(savedOwner);
        rate.setFilm(savedFilm);
        rate.setGrade(4);
        Rate savedRate = rateRepository.save(rate);

        //Act & Assert
        AccessDeniedException exception = Assertions.assertThrows(
                AccessDeniedException.class,
                () -> rateService.deleteRate(savedRate.getId(), savedGuest)
        );

        // Verify exception message
        Assertions.assertEquals("User does not have permission to delete this rate.", exception.getMessage());
        Assertions.assertFalse(rateRepository.findById(savedRate.getId()).isEmpty());
    }

}