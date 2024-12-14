package pk.ztp.filmbase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pk.ztp.filmbase.model.Film;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
}
