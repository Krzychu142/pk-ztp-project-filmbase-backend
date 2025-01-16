package pk.ztp.filmbase.service;

import org.springframework.data.domain.Page;
import pk.ztp.filmbase.dto.CommentDTO;
import pk.ztp.filmbase.model.User;

public interface ICommentService {
    CommentDTO saveComment(CommentDTO comment, User user);
    Page<CommentDTO> getAllCommentsByFilmId(long filmId, int pageNumber, int pageSize, String sortDirection);
    void deleteComment(long commentId, User user);
}
