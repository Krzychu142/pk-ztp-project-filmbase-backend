package pk.ztp.filmbase.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pk.ztp.filmbase.model.Rate;
import pk.ztp.filmbase.model.User;

import java.util.Optional;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
    Page<Rate> findByFilmId(Long filmId, Pageable pageable);
    Optional<Rate> findRateByUserAndFilmId(User user, long filmId);
}