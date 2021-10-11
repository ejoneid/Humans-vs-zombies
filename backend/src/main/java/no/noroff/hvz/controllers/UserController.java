package no.noroff.hvz.controllers;

import no.noroff.hvz.dto.AppUserDTO;
import no.noroff.hvz.mapper.Mapper;
import no.noroff.hvz.models.AppUser;
import no.noroff.hvz.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private Mapper mapper;
    @Autowired
    private UserService userService;

    private HttpStatus status = HttpStatus.OK;

    @GetMapping("/{id}")
    public ResponseEntity<AppUserDTO> getSpecificUser(@PathVariable Long id) {
        AppUser appUser = userService.getSpecificUser(id);
        AppUserDTO appUserDTO = null;
        if(appUser == null) {
            status = HttpStatus.NOT_FOUND;

        }
        else {
            appUserDTO = mapper.toAppUserDTO(appUser);
        }

        return new ResponseEntity<>(appUserDTO,status);
    }

    @PostMapping()
    public ResponseEntity<AppUserDTO> createUser(@RequestBody AppUserDTO userDTO) {
        AppUser appUser = mapper.toAppUser(userDTO);
        AppUserDTO addedUserDTO = mapper.toAppUserDTO(userService.createUser(appUser));
        status = HttpStatus.CREATED;
        return new ResponseEntity<>(addedUserDTO,status);
    }


}
