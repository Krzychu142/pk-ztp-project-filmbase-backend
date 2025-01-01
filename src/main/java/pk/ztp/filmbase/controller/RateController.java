package pk.ztp.filmbase.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
     * 3. GET AVERAGE OF RATES BY FILM ID
     * 4. GET RATE COUNT
     * */

    @PostMapping("/rate")
    public ResponseEntity<ApiResponseDTO> rateFilm(@Valid @RequestBody RateRequestDTO rateRequestDTO) {
        return ResponseEntity.ok().body(new ApiResponseDTO("ok",
                rateService.rateFilm(rateRequestDTO, authenticationFacade.getCurrentUser()))
        );
    }

    @DeleteMapping("/rate/{rateId}")
    public ResponseEntity<ApiResponseDTO> deleteRate(@PathVariable @Min(1) long rateId) {
        rateService.deleteRate(rateId, authenticationFacade.getCurrentUser());
        return ResponseEntity.ok().body(new ApiResponseDTO("ok", null));
    }

    @GetMapping("/film/{filmId}")
    public ResponseEntity<ApiResponseDTO> getAllRatesByFilmId(
            @RequestParam(name = "page-number", defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(name = "page-size", defaultValue = "5") @Min(1) @Max(15) int pageSize,
            @RequestParam(name = "sort-direction", defaultValue = "ASC") @Pattern(regexp = "ASC|DESC", message = "Sort direction must be 'ASC' or 'DESC'.") String sortDirection,
            @PathVariable @Min(1) long filmId
    ) {
        return ResponseEntity.ok().body(new ApiResponseDTO("ok",  rateService.getRates(pageNumber, pageSize, sortDirection, filmId)));
    }

}
