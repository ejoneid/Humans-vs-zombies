package no.noroff.hvz.controllers;

import no.noroff.hvz.dto.user.AppUserDTO;
import no.noroff.hvz.dto.user.AppUserDTOFull;
import no.noroff.hvz.dto.user.AppUserDTOReg;
import no.noroff.hvz.exceptions.AppUserAlreadyExistException;
import no.noroff.hvz.exceptions.AppUserNotFoundException;
import no.noroff.hvz.mapper.Mapper;
import no.noroff.hvz.models.AppUser;
import no.noroff.hvz.security.SecurityUtils;
import no.noroff.hvz.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class AppUserController {

    @Autowired
    private Mapper mapper;
    @Autowired
    private AppUserService appUserService;

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<List<AppUserDTOFull>> getAllUsers() {
        List<AppUser> users= appUserService.getAllUsers();
        List<AppUserDTOFull> userDtOs = users.stream().map(mapper::toAppUserDTOFull).collect(Collectors.toList());
        return  new ResponseEntity<>(userDtOs,HttpStatus.OK);
    }

    //TODO er dette sikkerhetsproblematisk? bør vi kun sende med openId i token? trenger jo bare å hente seg selv
    /**
     * Method for getting a specific user
     * @param openID opedId of requested User
     * @param principal Auth token som inneholder openID
     * @return the specific user DTO
     */
    @GetMapping("/{openID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AppUserDTO> getSpecificUser(@PathVariable String openID, @AuthenticationPrincipal Jwt principal) {
        AppUser appUser;
        // Her må det være try-catch fordi det skal returnes en annen httpstatus enn det som er vanlig for AppUserNotFoundException
        try {
            appUser = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
        } catch (AppUserNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        HttpStatus status = HttpStatus.OK;
        //Checks if the requester is admin or the same user
        if (SecurityUtils.isAdmin(principal.getTokenValue()) || openID.equals(principal.getClaimAsString("sub"))) {
            return new ResponseEntity<>(mapper.toAppUserDTOFull(appUser), status);
        }
        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
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
    public ResponseEntity<AppUserDTO> createUser(@RequestBody AppUserDTOReg userDTO, @AuthenticationPrincipal Jwt principal) throws DataIntegrityViolationException, AppUserAlreadyExistException {
        AppUser appUser = mapper.toAppUser(userDTO);
        appUser.setOpenId(principal.getClaimAsString("sub"));
        AppUser addedUser = appUserService.createUser(appUser);
        HttpStatus status = HttpStatus.CREATED;
        if (SecurityUtils.isAdmin(principal.getTokenValue())) {
            return new ResponseEntity<>(mapper.toAppUserDTOFull(addedUser), status);
        }
        return new ResponseEntity<>(mapper.toAppUserDTOReg(addedUser), status);
    }


}
