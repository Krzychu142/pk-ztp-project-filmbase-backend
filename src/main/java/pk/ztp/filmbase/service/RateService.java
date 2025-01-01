package pk.ztp.filmbase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pk.ztp.filmbase.dto.RateRequestDTO;
import pk.ztp.filmbase.dto.RateResponseDTO;
import pk.ztp.filmbase.dto.UserDTO;
import pk.ztp.filmbase.model.Film;
import pk.ztp.filmbase.model.Rate;
import pk.ztp.filmbase.model.User;
import pk.ztp.filmbase.repository.RateRepository;


@Service
@RequiredArgsConstructor
public class RateService implements IRateService, IDeletableResourceService<Rate> {

    private final IFilmService filmService;
    private final RateRepository rateRepository;

    @Override
    public RateResponseDTO rateFilm(RateRequestDTO rateRequestDTO, User user) {
        Film film = filmService.getFilmById(rateRequestDTO.getFilmId());
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
        return null;
    }

    @Override
    public Long getRateAverageByFilmId(long filmId) {
        return 0L;
    }

    @Override
    public int getRateCount(long filmId) {
        return 0;
    }
}
