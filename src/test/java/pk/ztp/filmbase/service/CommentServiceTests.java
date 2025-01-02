package pk.ztp.filmbase.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import pk.ztp.filmbase.dto.CommentDTO;
import pk.ztp.filmbase.enums.Genre;
import pk.ztp.filmbase.model.Comment;
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
        User user = new User();
        user.setUsername("testuser");
        User savedUser = userRepository.save(user);

        Film film = new Film();
        film.setTitle("Test Film");
        film.setGenre(Genre.ACTION);
        Film savedFilm = filmRepository.save(film);

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setComment("Test comment");
        commentDTO.setFilmId(savedFilm.getId());

        when(filmService.getFilmById(savedFilm.getId())).thenReturn(savedFilm);

        // Act
        CommentDTO savedComment = commentService.saveComment(commentDTO, savedUser);

        // Assert
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getComment()).isEqualTo("Test comment");
        assertThat(savedComment.getUser().getId()).isEqualTo(user.getId());
        assertThat(savedComment.getUser().getUsername()).isEqualTo(user.getUsername());

        verify(filmService, times(1)).getFilmById(savedFilm.getId());
    }

    @Test
    void shouldReturnAllCommentsByFilmId() {
        // Arrange
        Film film = new Film();
        film.setTitle("Test Film");
        film.setGenre(Genre.ACTION);
        Film savedFilm = filmRepository.save(film);

        User user = new User();
        user.setUsername("testuser");
        User savedUser = userRepository.save(user);

        Comment comment1 = new Comment("Comment 1", savedUser, savedFilm);
        Comment comment2 = new Comment("Comment 2", savedUser, savedFilm);
        commentRepository.save(comment1);
        commentRepository.save(comment2);

        int pageNumber = 0;
        int pageSize = 10;
        String sortDirection = "ASC";

        // Act
        Page<Comment> commentsPage = commentService.getAllCommentsByFilmId(film.getId(), pageNumber, pageSize, sortDirection);

        // Assert
        assertThat(commentsPage).isNotNull();
        assertThat(commentsPage.getTotalElements()).isEqualTo(2);
        assertThat(commentsPage.getContent().get(0).getComment()).isEqualTo("Comment 1");
        assertThat(commentsPage.getContent().get(1).getComment()).isEqualTo("Comment 2");
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwner() {
        User guest = new User();
        guest.setUsername("testuser");
        User savedGuest = userRepository.save(guest);

        User owner = new User();
        owner.setUsername("testuser2");
        User savedOwner = userRepository.save(owner);

        Film film = new Film();
        film.setTitle("Test Film");
        film.setGenre(Genre.ACTION);
        Film savedFilm = filmRepository.save(film);

        Comment comment1 = new Comment("Comment 1", savedOwner, savedFilm);
        Comment savedComment = commentRepository.save(comment1);

        //Act & Assert
        AccessDeniedException exception = Assertions.assertThrows(
                AccessDeniedException.class,
                () -> commentService.deleteComment(savedComment.getId(), savedGuest)
        );

        // Verify exception message
        Assertions.assertEquals("You do not have permission to delete this resource.", exception.getMessage());
        Assertions.assertFalse(commentRepository.findById(savedComment.getId()).isEmpty());
    }

}
