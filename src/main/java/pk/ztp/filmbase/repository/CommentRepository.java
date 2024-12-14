package pk.ztp.filmbase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pk.ztp.filmbase.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
