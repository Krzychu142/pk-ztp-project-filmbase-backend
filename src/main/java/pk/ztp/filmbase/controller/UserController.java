//package pk.ztp.filmbase.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import pk.ztp.filmbase.dto.ApiResponseDTO;
//import pk.ztp.filmbase.dto.UserDTO;
//import pk.ztp.filmbase.model.User;
//import pk.ztp.filmbase.repository.UserRepository;
//
//@RestController
//@RequestMapping("/users")
//@RequiredArgsConstructor
//public class UserController {
//    private final UserRepository userRepository;
//
//    @GetMapping("/user/{id}")
//    @PreAuthorize("#user.id == #id")
//    public ResponseEntity<ApiResponseDTO> getUser(@AuthenticationPrincipal User user, @PathVariable long id) {
//        return ResponseEntity.ok().body(new ApiResponseDTO("User", UserDTO.from(userRepository.findById(id).orElseThrow())));
//    }
//}
