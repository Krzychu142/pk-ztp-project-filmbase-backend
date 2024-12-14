package pk.ztp.filmbase.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pk.ztp.filmbase.dto.CommentDTO;
import pk.ztp.filmbase.model.Comment;
import pk.ztp.filmbase.model.Film;
import pk.ztp.filmbase.model.User;
import pk.ztp.filmbase.repository.CommentRepository;
import pk.ztp.filmbase.repository.FilmRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class CommentServiceTests {
    @Autowired
    private ICommentService commentService;

    @Autowired
    private FilmRepository filmRepository;

    @MockBean
    private CommentRepository commentRepository;

    @Test
    void shouldSaveComment() {
        // Arrange
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setComment("Test comment");
        commentDTO.setFilmId(1L);

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        Film film = new Film();
        film.setId(1L);
        film.setTitle("Test Film");

        Mockito.when(filmRepository.findById(1L)).thenReturn(Optional.of(film));

        // Act
        commentService.saveComment(commentDTO, user);

        // Assert
        ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).save(commentCaptor.capture());

        Comment savedComment = commentCaptor.getValue();
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getComment()).isEqualTo("Test comment");
        assertThat(savedComment.getUser()).isEqualTo(user);
    }
}
