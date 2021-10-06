package no.noroff.hvz.controllers;

import no.noroff.hvz.dto.PlayerDTO;
import no.noroff.hvz.mapper.Mapper;
import no.noroff.hvz.models.Game;
import no.noroff.hvz.models.Player;
import no.noroff.hvz.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/game/{gameID}/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<PlayerDTO>> getAllPlayers(@PathVariable Long gameID) {
        playerService.getAllPlayers(gameID);
        HttpStatus status;
        List<PlayerDTO> playerDTOs = new ArrayList<>();
        List<Player> players = playerService.getAllPlayers(gameID);
        if(players == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else{
            status = HttpStatus.OK;
            //TODO her må riktig DTO velges basert på admin
            playerDTOs = players.stream().map(mapper::toPlayerDTOFull).collect(Collectors.toList());
        }
        return new ResponseEntity<>(playerDTOs, status);
    }

    @GetMapping("/{playerID}")
    public ResponseEntity<PlayerDTO> getSpecificPlayer(@PathVariable Long gameID, @PathVariable Long playerID) {
        HttpStatus status;
        Player player = playerService.getSpecificPlayer(gameID, playerID);
        PlayerDTO playerDTO = mapper.toPlayerDTOFull(player);
        if(player.getId() == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.OK;
            //TODO her må riktig DTO velges basert på admin
        }
        return new ResponseEntity<>(playerDTO, status);
    }

    @PostMapping
    public ResponseEntity<PlayerDTO> createNewPlayer(@PathVariable Long gameID, @RequestBody Player player) {
        HttpStatus status;
        Player newPlayer = playerService.createNewPlayer(gameID, player);
        PlayerDTO playerDTO = mapper.toPlayerDTOFull(player);
        if(newPlayer.getId() == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.CREATED;
        }
        return new ResponseEntity<>(playerDTO,status);
    }

    @PutMapping("/{playerID}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long gameID, @PathVariable Long playerID, @RequestBody Player player) {
        return playerService.updatePlayer(gameID, playerID, player);
    }

    @DeleteMapping("/{playerID}")
    public ResponseEntity<Player> deletePlayer(@PathVariable Long gameID, @PathVariable Long playerID) {
        return playerService.deletePlayer(gameID, playerID);
    }
}
