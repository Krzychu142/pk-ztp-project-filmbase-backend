package pk.ztp.filmbase.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pk.ztp.filmbase.dto.ApiResponseDTO;
import pk.ztp.filmbase.dto.CommentDTO;
import pk.ztp.filmbase.security.IAuthenticationFacade;
import pk.ztp.filmbase.service.CommentService;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final IAuthenticationFacade authenticationFacade;
    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<ApiResponseDTO> saveComment(@Valid @RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok().body(new ApiResponseDTO("ok", commentService.saveComment(commentDTO, authenticationFacade.getCurrentUser())));
    }

}
