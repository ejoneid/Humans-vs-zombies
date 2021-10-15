package no.noroff.hvz.controllers;

import no.noroff.hvz.dto.player.PlayerDTO;
import no.noroff.hvz.dto.player.PlayerDTOPUT;
import no.noroff.hvz.dto.player.PlayerDTOReg;
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
     * @param authorization Auth token
     * @return list of players
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PlayerDTO>> getAllPlayers(@PathVariable Long gameID, @RequestHeader String authorization) {
        List<PlayerDTO> playerDTOs;
        Set<Player> players = playerService.getAllPlayers(gameID);

        // Checks if user is admin or not
        if (SecurityUtils.isAdmin(authorization)) {
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
     * @param authorization Auth token
     * @return the player
     */
    @GetMapping("/{playerID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PlayerDTO> getSpecificPlayer(@PathVariable Long gameID, @PathVariable Long playerID, @RequestHeader String authorization) {
        Player player = playerService.getSpecificPlayer(gameID, playerID);
        PlayerDTO playerDTO;
        // Checks if user is admin
        if (SecurityUtils.isAdmin(authorization)) {
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
     * @param authorization Auth token
     * @param principal Auth token
     * @return the created player
     * @throws AppUserNotFoundException if the provided user does not exists
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PlayerDTO> createNewPlayer(@PathVariable Long gameID, @RequestBody Optional<PlayerDTO> player,
                                                     @RequestHeader String authorization, @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException {
        HttpStatus status;
        Player newPlayer;
        PlayerDTO playerDTO;
        //Admins can specify some fields, while a normal user gets a default player
        if(SecurityUtils.isAdmin(authorization)) {
            newPlayer = playerService.createNewPlayer(gameID, mapper.regPlayerDTO((PlayerDTORegAdmin) player.get()));
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
        Player player = mapper.toPlayer(playerDTO, playerID);
        Player updatedPlayer = playerService.updatePlayer(gameID, playerID, player);
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

        HttpStatus status;
        Player deletedPlayer = playerService.deletePlayer(gameID, playerID);
        PlayerDTO playerDTO = null;
        if(deletedPlayer.getId() == null) {
            status = HttpStatus.NOT_FOUND;

        }
        else {
            status = HttpStatus.OK;
            playerDTO = mapper.toPlayerDTOFull(deletedPlayer);
        }

        return new ResponseEntity<>(playerDTO, status);
    }
}
