package pk.ztp.filmbase.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pk.ztp.filmbase.dto.RateRequestDTO;
import pk.ztp.filmbase.dto.RateResponseDTO;
import pk.ztp.filmbase.enums.Genre;
import pk.ztp.filmbase.model.Film;
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

}
