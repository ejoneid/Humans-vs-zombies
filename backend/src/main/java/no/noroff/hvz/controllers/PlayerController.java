package no.noroff.hvz.controllers;

import no.noroff.hvz.dto.player.PlayerDTO;
import no.noroff.hvz.dto.player.PlayerDTOPUT;
import no.noroff.hvz.dto.player.PlayerDTOReg;
import no.noroff.hvz.dto.squad.SquadDTO;
import no.noroff.hvz.mapper.Mapper;
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

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PlayerDTO> createNewPlayer(@PathVariable Long gameID, @RequestBody PlayerDTOReg player) {
        Player newPlayer = playerService.createNewPlayer(gameID, mapper.regPlayerDTO(player));
        PlayerDTO playerDTO = mapper.toPlayerDTOFull(newPlayer);
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(playerDTO,status);
    }

    @PutMapping("/{playerID}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable Long gameID, @PathVariable Long playerID, @RequestBody PlayerDTOPUT playerDto) {
        Player updatedPlayer = playerService.updatePlayer(gameID, playerID, playerDto);
        HttpStatus status = HttpStatus.OK;
        PlayerDTO playerDTO = mapper.toPlayerDTOFull(updatedPlayer);
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
