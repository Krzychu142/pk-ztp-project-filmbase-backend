package pk.ztp.filmbase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pk.ztp.filmbase.enums.Genre;
import pk.ztp.filmbase.exception.ResourceNotFound;
import pk.ztp.filmbase.model.Film;
import pk.ztp.filmbase.repository.FilmRepository;

@Service
@RequiredArgsConstructor
public class FilmService implements IFilmService {

    private final FilmRepository filmRepository;

    @Override
    public Film getFilmById(Long id) {
        return filmRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("No film found with id: " + id)
        );
    }

    @Override
    public Page<Film> getFilms(int pageNumber, int pageSize, String sortDirection, Genre genre) {
        //TODO: sort by rate average
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(sortDirection), "title"));
        if (genre != null) {
            return filmRepository.findByGenre(genre, pageable);
        }
        return filmRepository.findAll(pageable);
    }
}