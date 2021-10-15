package no.noroff.hvz.controllers;

import com.sun.net.httpserver.Headers;
import no.noroff.hvz.dto.game.GameDTO;
import no.noroff.hvz.dto.game.GameDTOReg;
import no.noroff.hvz.dto.game.GameDTOUpdate;
import no.noroff.hvz.dto.message.MessageDTO;
import no.noroff.hvz.dto.message.MessageDTOreg;
import no.noroff.hvz.exceptions.AppUserNotFoundException;
import no.noroff.hvz.mapper.Mapper;
import no.noroff.hvz.models.AppUser;
import no.noroff.hvz.models.Game;
import no.noroff.hvz.models.Message;
import no.noroff.hvz.models.Player;
import no.noroff.hvz.security.SecurityUtils;
import no.noroff.hvz.services.GameService;
import no.noroff.hvz.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/game")
// NB! Configure origins properly before deploy!
@CrossOrigin(origins = "*")
public class GameController {

    @Autowired
    private GameService gameService;
    @Autowired
    AppUserService appUserService;
    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<GameDTO>> getAllGames(@RequestParam Optional<String> state) {
        List<GameDTO> games;
        if (state.isPresent()) {
            games = gameService.getAllGames(state.get()).stream().map(mapper::toGameDTO).collect(Collectors.toList());
        } else {
            games = gameService.getAllGames().stream().map(mapper::toGameDTO).collect(Collectors.toList());
        }
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(games, status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDTO> getSpecificGame(@PathVariable Long id) throws NoSuchElementException {

        Game game = gameService.getSpecificGame(id);
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(mapper.toGameDTO(game),status);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<GameDTO> createNewGame(@RequestBody GameDTOReg gameDTOReg) {
        Game game = mapper.toGame(gameDTOReg);
        Game addedGame = gameService.createNewGame(game);
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(mapper.toGameDTO(addedGame), status);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<GameDTO> updateSpecificGame(@PathVariable Long id, @RequestBody GameDTOUpdate gameDTOUpdate) {
        HttpStatus status = HttpStatus.OK;
        Game game = mapper.toGame(gameDTOUpdate);
        Game updatedGame = gameService.updateSpecificGame(id, game);
        return new ResponseEntity<>(mapper.toGameDTO(updatedGame),status);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<GameDTO> deleteGame(@PathVariable Long id) {
        Game deletedGame = gameService.deleteGame(id);
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(mapper.toGameDTO(deletedGame), status);
    }

    @GetMapping("/{id}/chat")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MessageDTO>> getGameChat(@PathVariable Long id,
                                                     @RequestHeader(required = false) Long playerID,
                                                     @RequestHeader(required = false) String human,
                                                     @RequestHeader String authorization,
                                                     @AuthenticationPrincipal Jwt principal
                                                     ) throws NullPointerException, AppUserNotFoundException {
        HttpStatus status;
        List<Message> messages;
        List<MessageDTO> messageDTOs;
        if(SecurityUtils.isAdmin(authorization)) {
            if (playerID != null) messages = gameService.getGameChat(id, playerID);
            else if (human != null) messages = gameService.getGameChat(id, Boolean.valueOf(human));
            else messages = gameService.getGameChat(id);
        }
        else {
            AppUser user = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
            Player player = appUserService.getPlayerByGameAndUser(id, user);
            //TODO skal en vanlig spiller kunne hente ut messagene til noen andre ller bare seg selv?
            if (playerID != null && playerID.equals(player.getId())) messages = gameService.getGameChat(id, playerID);
            else messages = gameService.getGameChat(id, player.isHuman());
        }
        status = HttpStatus.OK;
         messageDTOs = messages.stream().map(mapper::toMessageDTO).collect(Collectors.toList());

        return new ResponseEntity<>(messageDTOs,status);
    }

    @PostMapping("/{id}/chat")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageDTO> createNewChat(@PathVariable Long id, @RequestBody MessageDTOreg message, @RequestHeader String authorization, @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException {
        AppUser appUser = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
        Player player = appUserService.getPlayerByGameAndUser(id, appUser);
        Message createdMessage = gameService.createNewChat(id, mapper.toMessage(message), appUser);
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(mapper.toMessageDTO(createdMessage), status);
    }
}
