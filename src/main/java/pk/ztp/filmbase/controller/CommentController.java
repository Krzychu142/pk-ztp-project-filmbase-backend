package pk.ztp.filmbase.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pk.ztp.filmbase.dto.ApiResponseDTO;
import pk.ztp.filmbase.security.IAuthenticationFacade;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final IAuthenticationFacade authenticationFacade;

    @PostMapping("/comment")
    public ResponseEntity<ApiResponseDTO> saveComment() {
        System.out.println(authenticationFacade.getCurrentUser().getId());
        return ResponseEntity.ok().body(new ApiResponseDTO("ok", null));
    }

}
