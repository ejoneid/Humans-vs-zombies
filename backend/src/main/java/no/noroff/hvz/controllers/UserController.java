package no.noroff.hvz.controllers;

import no.noroff.hvz.dto.AppUserDTO;
import no.noroff.hvz.mapper.Mapper;
import no.noroff.hvz.models.AppUser;
import no.noroff.hvz.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private Mapper mapper;
    @Autowired
    private UserService userService;

    private HttpStatus status = HttpStatus.OK;

    @GetMapping()
    public ResponseEntity<AppUserDTO> getSpecificUser(@AuthenticationPrincipal Jwt principal) {
        System.out.println(principal);
        AppUser appUser = userService.getSpecificUser(principal.getClaimAsString("sub"));
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
    public ResponseEntity<AppUserDTO> createUser(@RequestBody AppUserDTO userDTO, @AuthenticationPrincipal Jwt principal) {
        AppUser appUser = mapper.toAppUser(userDTO);
        appUser.setOpenId(principal.getClaimAsString("sub"));
        AppUserDTO addedUserDTO = mapper.toAppUserDTO(userService.createUser(appUser));
        status = HttpStatus.CREATED;
        return new ResponseEntity<>(addedUserDTO,status);
    }


}
