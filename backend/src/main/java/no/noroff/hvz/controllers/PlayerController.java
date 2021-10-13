package no.noroff.hvz.controllers;

import no.noroff.hvz.dto.player.PlayerDTO;
import no.noroff.hvz.dto.player.RegPlayerDTO;
import no.noroff.hvz.dto.squad.SquadDTO;
import no.noroff.hvz.dto.squad.SquadViewDTO;
import no.noroff.hvz.mapper.Mapper;
import no.noroff.hvz.models.Game;
import no.noroff.hvz.models.Player;
import no.noroff.hvz.models.Squad;
import no.noroff.hvz.security.SecurityUtils;
import no.noroff.hvz.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/game/{gameID}/player")
@CrossOrigin(origins = "*")
public class PlayerController {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private Mapper mapper;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PlayerDTO>> getAllPlayers(@PathVariable Long gameID, @RequestHeader String authorization) {
        playerService.getAllPlayers(gameID);
        HttpStatus status;
        List<PlayerDTO> playerDTOs = new ArrayList<>();
        List<Player> players = playerService.getAllPlayers(gameID);
        if(players == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else{
            status = HttpStatus.OK;
            if (SecurityUtils.isAdmin(authorization)) {
                playerDTOs = players.stream().map(mapper::toPlayerDTOFull).collect(Collectors.toList());
            }
            else {
                playerDTOs = players.stream().map(mapper::toPlayerDTOStandard).collect(Collectors.toList());
            }
        }
        return new ResponseEntity<>(playerDTOs, status);
    }

    @GetMapping("/{playerID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PlayerDTO> getSpecificPlayer(@PathVariable Long gameID, @PathVariable Long playerID, @RequestHeader String authorization) {
        HttpStatus status;
        Player player = playerService.getSpecificPlayer(gameID, playerID);
        PlayerDTO playerDTO = null;
        if(player.getId() == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.OK;
            if (SecurityUtils.isAdmin(authorization)) {
                playerDTO = mapper.toPlayerDTOFull(player);
            }
            else {
                playerDTO = mapper.toPlayerDTOStandard(player);
            }
        }
        return new ResponseEntity<>(playerDTO, status);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PlayerDTO> createNewPlayer(@PathVariable Long gameID, @RequestBody RegPlayerDTO player) {
        HttpStatus status;
        Player newPlayer = playerService.createNewPlayer(gameID, mapper.regPlayerDTO(player));
        if(newPlayer == null) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(null,status);
        }
        else {
            PlayerDTO playerDTO = mapper.toPlayerDTOFull(newPlayer);
            status = HttpStatus.CREATED;
            return new ResponseEntity<>(playerDTO,status);
        }
    }

    @PutMapping("/{playerID}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable Long gameID, @PathVariable Long playerID, @RequestBody Player player) {
        HttpStatus status;
        if(!Objects.equals(playerID,player.getId())) {
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(mapper.toPlayerDTOStandard(new Player()),status);
        }
        Player updatedPlayer = playerService.updatePlayer(gameID, playerID, player);
        PlayerDTO playerDTO = null;
        if(updatedPlayer.getId() == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.OK;
            playerDTO = mapper.toPlayerDTOFull(player);
        }
        return new ResponseEntity<>(playerDTO, status);
    }

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

    @GetMapping("/{playerID}/squad")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SquadDTO> getPlayerSquad(@PathVariable Long gameID, @PathVariable Long playerID) {
        HttpStatus status;
        Squad squad = playerService.getPlayerSquad(gameID, playerID);
        if (squad != null) {
            status = HttpStatus.OK;
            return new ResponseEntity<>(mapper.toSquadDTO(squad), status);
        } else {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(null, status);
        }
    }
}
