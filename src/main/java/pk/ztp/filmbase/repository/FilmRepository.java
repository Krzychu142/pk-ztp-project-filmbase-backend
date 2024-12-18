package pk.ztp.filmbase.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pk.ztp.filmbase.enums.Genre;
import pk.ztp.filmbase.model.Film;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
    Page<Film> findByGenre(Genre genre, Pageable pageable);
}