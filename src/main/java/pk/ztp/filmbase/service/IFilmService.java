package pk.ztp.filmbase.service;

import org.springframework.data.domain.Page;
import pk.ztp.filmbase.enums.Genre;
import pk.ztp.filmbase.model.Film;

public interface IFilmService {
    Film getFilmById(Long id);
    Page<Film> getFilms(int pageNumber, int pageSize, String sortDirection, Genre genre);
}