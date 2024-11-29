package pk.ztp.filmbase.service;

import pk.ztp.filmbase.dto.CommentDTO;
import pk.ztp.filmbase.model.User;

public interface ICommentService {
    void saveComment(CommentDTO comment, User user);
}
