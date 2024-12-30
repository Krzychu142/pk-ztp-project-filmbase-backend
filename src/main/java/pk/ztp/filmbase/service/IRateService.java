package pk.ztp.filmbase.service;

import org.springframework.data.domain.Page;
import pk.ztp.filmbase.model.Rate;

public interface IRateService {
    void rateFilm();
    Page<Rate> getRates(int pageNumber, int pageSize, String sortDirection, long filmId);
    Long getRateAverageByFilmId(long filmId);
    int getRateCount(long filmId);
}
