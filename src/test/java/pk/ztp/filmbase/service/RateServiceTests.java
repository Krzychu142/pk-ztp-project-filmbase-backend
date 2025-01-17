package pk.ztp.filmbase.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import pk.ztp.filmbase.dto.RateRequestDTO;
import pk.ztp.filmbase.dto.RateResponseDTO;
import pk.ztp.filmbase.enums.Genre;
import pk.ztp.filmbase.exception.ResourceAlreadyExist;
import pk.ztp.filmbase.model.Film;
import pk.ztp.filmbase.model.Rate;
import pk.ztp.filmbase.model.User;
import pk.ztp.filmbase.repository.FilmRepository;
import pk.ztp.filmbase.repository.RateRepository;
import pk.ztp.filmbase.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> rateService.deleteRate(savedRate.getId(), savedGuest)
        );

        // Verify exception message
        Assertions.assertEquals("You do not have permission to delete this resource.", exception.getMessage());
        Assertions.assertFalse(rateRepository.findById(savedRate.getId()).isEmpty());
    }

    @Test
    void shouldReturnRatesWhenFilmExistsAndHasRates(){
        // Arrange
        int maxGrade = 5;
        int midGrade = 3;
        int minGrade = 1;

        User user = new User();
        user.setUsername("testuser");
        User savedUser = userRepository.save(user);

        User user2 = new User();
        user2.setUsername("testuser2");
        User savedUser2 = userRepository.save(user2);

        User user3 = new User();
        user3.setUsername("testuser3");
        User savedUser3 = userRepository.save(user3);

        Film film = new Film();
        film.setTitle("Test Film");
        film.setGenre(Genre.ACTION);
        Film savedFilm = filmRepository.save(film);

        Rate rateMax = new Rate();
        rateMax.setUser(savedUser);
        rateMax.setFilm(savedFilm);
        rateMax.setGrade(maxGrade);
        Rate savedRateMax = rateRepository.save(rateMax);

        Rate rateMid = new Rate();
        rateMid.setUser(savedUser2);
        rateMid.setFilm(savedFilm);
        rateMid.setGrade(midGrade);
        Rate savedRateMid = rateRepository.save(rateMid);

        Rate rateMin = new Rate();
        rateMin.setUser(savedUser3);
        rateMin.setFilm(savedFilm);
        rateMin.setGrade(minGrade);
        Rate savedRateMin = rateRepository.save(rateMin);

        //Act
        Page<Rate> resultRates = rateService.getRates(0, 3, "ASC", savedFilm.getId());

        //Assert
        Assertions.assertNotNull(resultRates);
        Assertions.assertEquals(3, resultRates.getTotalElements());
        Assertions.assertEquals(3, resultRates.getNumberOfElements());
        Assertions.assertEquals(savedRateMax.getGrade(), resultRates.getContent().get(2).getGrade());
        Assertions.assertEquals(savedRateMid.getGrade(), resultRates.getContent().get(1).getGrade());
        Assertions.assertEquals(savedRateMin.getGrade(), resultRates.getContent().get(0).getGrade());
    }

    @Test
    void shouldReturnCountOfRates3WhenFilmHas3Rates(){
        // Arrange
        int maxGrade = 5;
        int midGrade = 3;
        int minGrade = 1;

        User user = new User();
        user.setUsername("testuser");
        User savedUser = userRepository.save(user);

        User user2 = new User();
        user2.setUsername("testuser2");
        User savedUser2 = userRepository.save(user2);

        User user3 = new User();
        user3.setUsername("testuser3");
        User savedUser3 = userRepository.save(user3);

        Film film = new Film();
        film.setTitle("Test Film");
        film.setGenre(Genre.ACTION);
        Film savedFilm = filmRepository.save(film);

        Rate firstRate = new Rate();
        firstRate.setUser(savedUser);
        firstRate.setFilm(savedFilm);
        firstRate.setGrade(maxGrade);
        rateRepository.save(firstRate);

        Rate secondRate = new Rate();
        secondRate.setUser(savedUser2);
        secondRate.setFilm(savedFilm);
        secondRate.setGrade(midGrade);
        rateRepository.save(secondRate);

        Rate thirdRate = new Rate();
        thirdRate.setUser(savedUser3);
        thirdRate.setFilm(savedFilm);
        thirdRate.setGrade(minGrade);
        rateRepository.save(thirdRate);

        //Act
        long countOfRates = rateService.getRateCountByFilmId(savedFilm.getId());

        //Assert
        Assertions.assertEquals(3, countOfRates);
    }

    @Test
    void shouldReturnCount0WhenNoRatesFound(){
        //Arrange
        User user = new User();
        user.setUsername("testuser");
        userRepository.save(user);

        Film film = new Film();
        film.setTitle("Test Film");
        film.setGenre(Genre.ACTION);
        Film savedFilm = filmRepository.save(film);

        //Act
        long countOfRates = rateService.getRateCountByFilmId(savedFilm.getId());

        //Assert
        Assertions.assertEquals(0, countOfRates);
    }

    @Test
    void shouldReturn3WhenSumOf3RatesAreEqual9(){
        // Arrange
        int maxGrade = 5;
        int midGrade = 3;
        int minGrade = 1;

        User user = new User();
        user.setUsername("testuser");
        User savedUser = userRepository.save(user);

        User user2 = new User();
        user2.setUsername("testuser2");
        User savedUser2 = userRepository.save(user2);

        User user3 = new User();
        user3.setUsername("testuser3");
        User savedUser3 = userRepository.save(user3);

        Film film = new Film();
        film.setTitle("Test Film");
        film.setGenre(Genre.ACTION);
        Film savedFilm = filmRepository.save(film);

        Rate firstRate = new Rate();
        firstRate.setUser(savedUser);
        firstRate.setFilm(savedFilm);
        firstRate.setGrade(maxGrade);
        rateRepository.save(firstRate);

        Rate secondRate = new Rate();
        secondRate.setUser(savedUser2);
        secondRate.setFilm(savedFilm);
        secondRate.setGrade(midGrade);
        rateRepository.save(secondRate);

        Rate thirdRate = new Rate();
        thirdRate.setUser(savedUser3);
        thirdRate.setFilm(savedFilm);
        thirdRate.setGrade(minGrade);
        rateRepository.save(thirdRate);

        //Act
        double averageOfRates = rateService.getRateAverageByFilmId(savedFilm.getId());

        //Assert
        Assertions.assertEquals(3.0, averageOfRates);
    }

    @Test
    void shouldReturnAverage0WhenNoRatesFound(){
        //Arrange
        User user = new User();
        user.setUsername("testuser");
        userRepository.save(user);

        Film film = new Film();
        film.setTitle("Test Film");
        film.setGenre(Genre.ACTION);
        Film savedFilm = filmRepository.save(film);

        //Act
        double averageOfRates = rateService.getRateAverageByFilmId(savedFilm.getId());

        //Assert
        Assertions.assertEquals(0.0, averageOfRates);
    }

    @Transactional
    @Test
    void one_user_should_be_able_to_rate_only_one_time_one_film(){
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        userRepository.save(user);

        Film film = new Film();
        film.setTitle("Test Film");
        film.setGenre(Genre.ACTION);
        Film savedFilm = filmRepository.save(film);

        RateRequestDTO rateRequestDTO = new RateRequestDTO();
        rateRequestDTO.setFilmId(savedFilm.getId());
        rateRequestDTO.setGrade(4);

        // Act
        rateService.rateFilm(rateRequestDTO, user);

        // Assert
        assertThrows(ResourceAlreadyExist.class, () -> {
            rateService.rateFilm(rateRequestDTO, user);
        });

        Page<Rate> resultRates = rateService.getRates(0, 3, "ASC", savedFilm.getId());
        List<Rate> rates = resultRates.getContent();

        Assertions.assertEquals(1, rates.size());
        Assertions.assertEquals(4, rates.get(0).getGrade());
        Assertions.assertEquals("testuser", rates.get(0).getUser().getUsername());
    }

}
