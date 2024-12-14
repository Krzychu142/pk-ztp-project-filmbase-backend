package pk.ztp.filmbase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
}