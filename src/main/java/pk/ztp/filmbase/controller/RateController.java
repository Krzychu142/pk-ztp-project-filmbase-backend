package pk.ztp.filmbase.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pk.ztp.filmbase.dto.ApiResponseDTO;
import pk.ztp.filmbase.dto.RateRequestDTO;
import pk.ztp.filmbase.security.IAuthenticationFacade;
import pk.ztp.filmbase.service.IRateService;

@RestController
@RequestMapping("/rates")
@RequiredArgsConstructor
public class RateController {

    private final IRateService rateService;
    private final IAuthenticationFacade authenticationFacade;

    /**
     * TODO:
     * 1. RATE FILM
     * -- DELETE RATE BY RATE ID
     * 2. GET ALL RATES BY FILM ID
     * 3. GET AVERAGE OF RATES BY FILM ID
     * 4. GET RATE COUNT
     * */

    @PostMapping("/rate")
    public ResponseEntity<ApiResponseDTO> rateFilm(@Valid @RequestBody RateRequestDTO rateRequestDTO) {
        return ResponseEntity.ok().body(new ApiResponseDTO("ok",
                rateService.rateFilm(rateRequestDTO, authenticationFacade.getCurrentUser()))
        );
    }

}
