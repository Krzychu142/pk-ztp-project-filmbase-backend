package pk.ztp.filmbase.service;

import org.springframework.data.domain.Page;
import pk.ztp.filmbase.dto.RateRequestDTO;
import pk.ztp.filmbase.dto.RateResponseDTO;
import pk.ztp.filmbase.model.Rate;
import pk.ztp.filmbase.model.User;

public interface IRateService {
    RateResponseDTO rateFilm(RateRequestDTO rateRequestDTO, User user);
    void deleteRate(long rateId, User user);
    Page<Rate> getRates(int pageNumber, int pageSize, String sortDirection, long filmId);
    double getRateAverageByFilmId(long filmId);
    long getRateCountByFilmId(long filmId);
}
