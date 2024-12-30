package pk.ztp.filmbase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pk.ztp.filmbase.model.Rate;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
}
