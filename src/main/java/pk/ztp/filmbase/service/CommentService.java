package pk.ztp.filmbase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pk.ztp.filmbase.dto.CommentDTO;
import pk.ztp.filmbase.dto.UserDTO;
import pk.ztp.filmbase.model.Comment;
import pk.ztp.filmbase.model.User;
import pk.ztp.filmbase.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService, IDeletableResourceService<Comment> {

    private final CommentRepository commentRepository;
    private final IFilmService filmService;

    @Override
    public CommentDTO saveComment(CommentDTO comment, User user) {
        Comment newComment = new Comment(comment.getComment(), user, filmService.getFilmById(comment.getFilmId()));
        Comment savedComment = commentRepository.save(newComment);
        return new CommentDTO(savedComment.getId(), savedComment.getComment(), comment.getFilmId(), savedComment.getCreatedAt(), UserDTO.from(user));
    }

    @Transactional
    @Override
    public Page<CommentDTO> getAllCommentsByFilmId(long filmId, int pageNumber, int pageSize, String sortDirection) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(sortDirection), "createdAt"));
        Page<Comment> commentsPage = commentRepository.findByFilmId(filmId, pageable);

        return commentsPage.map(comment -> new CommentDTO(
                comment.getId(),
                comment.getComment(),
                comment.getFilm().getId(),
                comment.getCreatedAt(),
                UserDTO.from(comment.getUser())
        ));
    }

    @Override
    public void deleteComment(long commentId, User user) {
        deleteResource(
                commentId,
                user,
                commentRepository::findById,
                comment -> comment.getUser().getId(),
                commentRepository::delete
        );
    }

}
