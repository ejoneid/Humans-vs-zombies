package no.noroff.hvz.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
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

    /**
     * Method for getting all users. Admin only.
     * @return List of appUser DTOs
     */
    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    @Tag(name = "getAllUsers", description = "Method for getting all users. Admin only")
    public ResponseEntity<List<AppUserDTOFull>> getAllUsers() {
        List<AppUser> users= appUserService.getAllUsers();
        List<AppUserDTOFull> userDTOs = users.stream().map(user -> mapper.toAppUserDTOFull(user, false)).collect(Collectors.toList());
        return  new ResponseEntity<>(userDTOs,HttpStatus.OK);
    }

    /**
     * Method for getting your user
     * @param principal Auth token with openID
     * @return the specific user DTO
     */
    @GetMapping("/log-in")
    @PreAuthorize("isAuthenticated()")
    @Tag(name = "getCurrentUser", description = "Method for getting the current user from the database. Uses auth0 token to identify user")
    public ResponseEntity<AppUserDTOFull> getSpecificUser( @AuthenticationPrincipal Jwt principal) {
        AppUser appUser;
        // try catch because we send a different Http status code than usually with AppUserNotFoundException
        try {
            appUser = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
        } catch (AppUserNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(mapper.toAppUserDTOFull(appUser, SecurityUtils.isAdmin(principal.getTokenValue())), status);
    }

    /**
     * Method for creating a new user
     * @param userDTO DTO for a new user
     * @param principal Auth token
     * @return the created user DTO
     * @throws DataIntegrityViolationException when the user already exists
     */
    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    @Tag(name = "createUser", description = "Method for creating a user in the database for the current user.")
    public ResponseEntity<AppUserDTOFull> createUser(@RequestBody AppUserDTOReg userDTO, @AuthenticationPrincipal Jwt principal) throws DataIntegrityViolationException, AppUserAlreadyExistException {
        AppUser appUser = mapper.toAppUser(userDTO);
        appUser.setOpenId(principal.getClaimAsString("sub"));
        AppUser addedUser = appUserService.createUser(appUser);
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(mapper.toAppUserDTOFull(addedUser, SecurityUtils.isAdmin(principal.getTokenValue())), status);
    }


}
