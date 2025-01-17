package pk.ztp.filmbase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pk.ztp.filmbase.dto.RateRequestDTO;
import pk.ztp.filmbase.dto.RateResponseDTO;
import pk.ztp.filmbase.dto.UserDTO;
import pk.ztp.filmbase.exception.ResourceAlreadyExist;
import pk.ztp.filmbase.exception.ResourceNotFound;
import pk.ztp.filmbase.model.Film;
import pk.ztp.filmbase.model.Rate;
import pk.ztp.filmbase.model.User;
import pk.ztp.filmbase.repository.RateRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RateService implements IRateService, IDeletableResourceService<Rate> {

    private final IFilmService filmService;
    private final RateRepository rateRepository;

    @Override
    public RateResponseDTO rateFilm(RateRequestDTO rateRequestDTO, User user) {
        Long filmId = rateRequestDTO.getFilmId();
        Optional<Rate> optionalRate = rateRepository.findRateByUserAndFilmId(user, filmId);
        if (optionalRate.isPresent()) {
            throw new ResourceAlreadyExist("You have already rated this film. Please delete your previous rating before submitting a new one.");
        }
        Film film = filmService.getFilmById(filmId);
        Rate rate = new Rate(rateRequestDTO.getGrade(), user, film);
        Rate savedRate = rateRepository.save(rate);
        return new RateResponseDTO(
                savedRate.getId(),
                savedRate.getGrade(),
                film.getId(),
                UserDTO.from(user)
        );
    }

    @Override
    public void deleteRate(long rateId, User user) {
        deleteResource(
                rateId,
                user,
                rateRepository::findById,
                rate -> rate.getUser().getId(),
                rateRepository::delete
        );
    }

    @Override
    public Page<Rate> getRates(int pageNumber, int pageSize, String sortDirection, long filmId) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(sortDirection), "grade"));
        return rateRepository.findByFilmId(filmId, pageable);
    }

    @Override
    public double getRateAverageByFilmId(long filmId) {
        List<Rate> rates = getRatesByFilmId(filmId);
        if (rates.isEmpty()) {
            return 0.0;
        }
        return (double) getRateSum(rates) / rates.size();
    }

    private int getRateSum(List<Rate> rates) {
        return rates.stream().mapToInt(Rate::getGrade).sum();
    }

    @Override
    public long getRateCountByFilmId(long filmId) {
        return getRatesByFilmId(filmId).size();
    }

    @Override
    public RateResponseDTO getRateByUserAndFilmId(User user, long filmId) {
        Optional<Rate> optionalRate = rateRepository.findRateByUserAndFilmId(user, filmId);
        if (optionalRate.isPresent()) {
            Rate rate = optionalRate.get();
            return new RateResponseDTO(
                    rate.getId(),
                    rate.getGrade(),
                    filmId,
                    UserDTO.from(user)
            );
        }
        return null;
    }

    @Override
    public RateResponseDTO updateRate(long rateId, RateRequestDTO rateRequestDTO, User user) {
        Optional<Rate> optionalRate = rateRepository.findRateById(rateId);
        if (optionalRate.isEmpty()) {
            throw new ResourceNotFound("Rate with id: " + rateId + "not found");
        }
        Rate rate = optionalRate.get();
        if (!Objects.equals(rate.getUser().getId(), user.getId())) {
            throw new AccessDeniedException("You do not have permission to update this rate.");
        }
        // TODO: check is update necessary
        rate.setGrade(rateRequestDTO.getGrade());
        Rate updatedRate = rateRepository.save(rate);
         return new RateResponseDTO(
                rate.getId(),
                rate.getGrade(),
                rateRequestDTO.getFilmId(),
                UserDTO.from(user)
        );
    }

    private List<Rate> getRatesByFilmId(long filmId) {
        return filmService.getFilmById(filmId).getRating();
    }
}