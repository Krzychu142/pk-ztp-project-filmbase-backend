package pk.ztp.filmbase.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pk.ztp.filmbase.dto.ApiResponseDTO;
import pk.ztp.filmbase.dto.CommentDTO;
import pk.ztp.filmbase.security.IAuthenticationFacade;
import pk.ztp.filmbase.service.ICommentService;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final IAuthenticationFacade authenticationFacade;
    private final ICommentService commentService;

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<ApiResponseDTO> deleteComment(@PathVariable @Min(1) long commentId) {
        commentService.deleteComment(commentId, authenticationFacade.getCurrentUser());
        return ResponseEntity.ok().body(new ApiResponseDTO("ok", null));
    }

    @PostMapping("/comment")
    public ResponseEntity<ApiResponseDTO> saveComment(@Valid @RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok().body(new ApiResponseDTO("ok", commentService.saveComment(commentDTO, authenticationFacade.getCurrentUser())));
    }

    @GetMapping("/film/{filmId}")
    public ResponseEntity<ApiResponseDTO> getAllCommentsByFilmId(
            @Min(1) @PathVariable long filmId,
            @RequestParam(name = "page-number", defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(name = "page-size", defaultValue = "5") @Min(1) @Max(15) int pageSize,
            @RequestParam(name = "sort-direction", defaultValue = "ASC") @Pattern(regexp = "ASC|DESC", message = "Sort direction must be 'ASC' or 'DESC'.") String sortDirection
    ) {
        return ResponseEntity.ok().body(new ApiResponseDTO("ok", commentService.getAllCommentsByFilmId(filmId, pageNumber, pageSize, sortDirection)));
    }

}
