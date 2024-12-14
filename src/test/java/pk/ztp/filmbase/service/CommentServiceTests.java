package pk.ztp.filmbase.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pk.ztp.filmbase.dto.CommentDTO;
import pk.ztp.filmbase.enums.Genre;
import pk.ztp.filmbase.model.Film;
import pk.ztp.filmbase.model.User;
import pk.ztp.filmbase.repository.CommentRepository;
import pk.ztp.filmbase.repository.FilmRepository;
import pk.ztp.filmbase.repository.UserRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CommentServiceTests {
    @Autowired
    private ICommentService commentService;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @MockBean
    private IFilmService filmService;

    @AfterEach
    public void tearDown() {
        filmRepository.deleteAll();
        userRepository.deleteAll();
        commentRepository.deleteAll();
    }

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
        film.setGenre(Genre.ACTION);

        filmRepository.save(film);
        userRepository.save(user);
        when(filmService.getFilmById(1L)).thenReturn(film);

        // Act
        CommentDTO savedComment = commentService.saveComment(commentDTO, user);

        // Assert
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getComment()).isEqualTo("Test comment");
        assertThat(savedComment.getUser().getId()).isEqualTo(user.getId());
        assertThat(savedComment.getUser().getUsername()).isEqualTo(user.getUsername());

        verify(filmService, times(1)).getFilmById(1L);
    }
}
