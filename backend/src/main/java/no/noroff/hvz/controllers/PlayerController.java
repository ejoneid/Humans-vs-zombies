package no.noroff.hvz.controllers;

import no.noroff.hvz.dto.player.PlayerDTO;
import no.noroff.hvz.dto.squad.SquadDTO;
import no.noroff.hvz.dto.player.PlayerDTORegAdmin;
import no.noroff.hvz.dto.player.PlayerDTOUpdate;
import no.noroff.hvz.exceptions.AppUserNotFoundException;
import no.noroff.hvz.mapper.Mapper;
import no.noroff.hvz.models.AppUser;
import no.noroff.hvz.models.Player;
import no.noroff.hvz.security.SecurityUtils;
import no.noroff.hvz.services.AppUserService;
import no.noroff.hvz.services.PlayerService;
import org.hibernate.result.NoMoreReturnsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/game/{gameID}/player")
@CrossOrigin(origins = "*")
public class PlayerController {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private Mapper mapper;
    @Autowired
    private AppUserService appUserService;

    /**
     * Method for getting all players
     * @param gameID ID of game
     * @param principal Auth token
     * @return list of players
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PlayerDTO>> getAllPlayers(@PathVariable Long gameID, @AuthenticationPrincipal Jwt principal) {
        List<PlayerDTO> playerDTOs;
        Set<Player> players = playerService.getAllPlayers(gameID);

        // Checks if user is admin or not
        if (SecurityUtils.isAdmin(principal.getTokenValue())) {
            playerDTOs = players.stream().map(mapper::toPlayerDTOFull).collect(Collectors.toList());
        }
        else {
            playerDTOs = players.stream().map(mapper::toPlayerDTOStandard).collect(Collectors.toList());
        }
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(playerDTOs, status);
    }

    /**
     * Method for getting a specific player
     * @param gameID ID of game
     * @param playerID ID of player
     * @param principal Auth token
     * @return the player
     */
    @GetMapping("/{playerID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PlayerDTO> getSpecificPlayer(@PathVariable Long gameID, @PathVariable Long playerID, @AuthenticationPrincipal Jwt principal) {
        Player player = playerService.getSpecificPlayer(gameID, playerID);
        PlayerDTO playerDTO;
        // Checks if user is admin
        if (SecurityUtils.isAdmin(principal.getTokenValue())) {
            playerDTO = mapper.toPlayerDTOFull(player);
        }
        else {
            playerDTO = mapper.toPlayerDTOStandard(player);
        }
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(playerDTO, status);
    }

    /**
     * Method for creating a new player, different for an admin
     * @param gameID ID of game
     * @param player Optinioal DTO with info about the new player
     * @param principal Auth token
     * @return the created player
     * @throws AppUserNotFoundException if the provided user does not exists
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PlayerDTO> createNewPlayer(@PathVariable Long gameID, @RequestBody Optional<PlayerDTORegAdmin> player,
                                                      @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException {
        HttpStatus status;
        Player newPlayer;
        PlayerDTO playerDTO;
        //Admins can specify some fields, while a normal user gets a default player
        if(SecurityUtils.isAdmin(principal.getTokenValue())) {
            newPlayer = playerService.createNewPlayer(gameID, mapper.regPlayerDTO( player.get()));
            playerDTO = mapper.toPlayerDTOFull(newPlayer);
        }
        else {
            AppUser user = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
            newPlayer = playerService.createNewPlayer(gameID, user);
            playerDTO = mapper.toPlayerDTOStandard(newPlayer);
        }

        status = HttpStatus.CREATED;
        return new ResponseEntity<>(playerDTO,status);
    }

    /**
     * Method for updating a player, admin only
     * @param gameID ID of game
     * @param playerID ID of player
     * @param playerDTO DTO with new info
     * @return the updated player
     */
    @PutMapping("/{playerID}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable Long gameID, @PathVariable Long playerID,
                                                  @RequestBody PlayerDTOUpdate playerDTO) {
        HttpStatus status;
        Player updatedPlayer = playerService.updatePlayer(gameID, playerID, playerDTO);
        PlayerDTO updatedPlayerDTO = mapper.toPlayerDTOFull(updatedPlayer);
        status = HttpStatus.OK;
        return new ResponseEntity<>(updatedPlayerDTO, status);
    }

    /**
     * Method for deleting a player, admin only
     * @param gameID ID of game
     * @param playerID ID of player
     * @return the deleted player
     */
    @DeleteMapping("/{playerID}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<PlayerDTO> deletePlayer(@PathVariable Long gameID, @PathVariable Long playerID) {
        Player deletedPlayer = playerService.deletePlayer(gameID, playerID);
        HttpStatus status = HttpStatus.OK;
        PlayerDTO playerDTO = mapper.toPlayerDTOFull(deletedPlayer);
        return new ResponseEntity<>(playerDTO, status);
    }
}
