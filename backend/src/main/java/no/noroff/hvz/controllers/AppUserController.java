package no.noroff.hvz.controllers;

import no.noroff.hvz.dto.AppUserDTO;
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

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AppUserDTO> getSpecificUser(@AuthenticationPrincipal Jwt principal) {
        System.out.println(principal);
        AppUser appUser = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
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

    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AppUserDTO> createUser(@RequestBody AppUserDTO userDTO, @AuthenticationPrincipal Jwt principal) {
        AppUser appUser = mapper.toAppUser(userDTO);
        appUser.setOpenId(principal.getClaimAsString("sub"));
        AppUserDTO addedUserDTO;
        try {
            addedUserDTO = mapper.toAppUserDTO(appUserService.createUser(appUser));
        } catch (DataIntegrityViolationException exception) {
            status = HttpStatus.CONFLICT;
            return new ResponseEntity<>(null, status);
        }
         addedUserDTO = mapper.toAppUserDTO(appUserService.createUser(appUser));
        status = HttpStatus.CREATED;
        return new ResponseEntity<>(addedUserDTO,status);
    }


}