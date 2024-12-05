package pk.ztp.filmbase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pk.ztp.filmbase.dto.CommentDTO;
import pk.ztp.filmbase.model.User;
import pk.ztp.filmbase.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;

    @Override
    public void saveComment(CommentDTO comment, User user) {

    }
}
