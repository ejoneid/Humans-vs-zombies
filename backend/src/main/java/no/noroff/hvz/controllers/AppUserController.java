package no.noroff.hvz.controllers;

import no.noroff.hvz.dto.user.AppUserDTO;
import no.noroff.hvz.exceptions.AppUserNotFoundException;
import no.noroff.hvz.mapper.Mapper;
import no.noroff.hvz.models.AppUser;
import org.hibernate.exception.ConstraintViolationException;
import no.noroff.hvz.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class AppUserController {

    @Autowired
    private Mapper mapper;
    @Autowired
    private AppUserService appUserService;

    private HttpStatus status = HttpStatus.OK;

    /**
     * Method for getting a specific user
     * @param principal Auth token som inneholder openID
     * @return the specific user DTO
     */
    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AppUserDTO> getSpecificUser(@AuthenticationPrincipal Jwt principal) {
        AppUser appUser;
        try {
            appUser = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
        } catch (AppUserNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        AppUserDTO appUserDTO = null;
        if(appUser == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.OK;
            appUserDTO = mapper.toAppUserDTO(appUser);
        }
        return new ResponseEntity<>(appUserDTO,status);
    }

    /**
     * Method for creating a new user
     * @param userDTO DTO for a new user
     * @param principal Auth token
     * @return the created user DTO
     * @throws DataIntegrityViolationException
     */
    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AppUserDTO> createUser(@RequestBody AppUserDTO userDTO, @AuthenticationPrincipal Jwt principal) throws DataIntegrityViolationException {
        AppUser appUser = mapper.toAppUser(userDTO);
        appUser.setOpenId(principal.getClaimAsString("sub"));
        AppUserDTO addedUserDTO = mapper.toAppUserDTO(appUserService.createUser(appUser));
        status = HttpStatus.CREATED;
        return new ResponseEntity<>(addedUserDTO,status);
    }


}
