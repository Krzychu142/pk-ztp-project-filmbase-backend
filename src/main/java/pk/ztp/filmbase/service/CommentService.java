package pk.ztp.filmbase.service;

import org.springframework.stereotype.Service;
import pk.ztp.filmbase.dto.CommentDTO;
import pk.ztp.filmbase.model.User;

@Service
public class CommentService implements ICommentService {
    @Override
    public void saveComment(CommentDTO comment, User user) {
        System.out.println(comment);
        System.out.println(user);
    }
}
