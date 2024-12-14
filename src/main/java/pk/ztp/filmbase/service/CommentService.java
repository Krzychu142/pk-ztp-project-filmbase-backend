package pk.ztp.filmbase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pk.ztp.filmbase.dto.CommentDTO;
import pk.ztp.filmbase.dto.UserDTO;
import pk.ztp.filmbase.model.Comment;
import pk.ztp.filmbase.model.User;
import pk.ztp.filmbase.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;
    private final IFilmService filmService;

    @Override
    public CommentDTO saveComment(CommentDTO comment, User user) {
        Comment newComment = new Comment(comment.getComment(), user, filmService.getFilmById(comment.getFilmId()));
        Comment savedComment = commentRepository.save(newComment);
        return new CommentDTO(savedComment.getId(), savedComment.getComment(), comment.getFilmId(), UserDTO.from(user));
    }
}
