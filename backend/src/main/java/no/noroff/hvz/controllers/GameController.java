package no.noroff.hvz.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
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
    @Tag(name = "getAllGames", description = "API for getting all games, optional parameter state -> only returns games with provided state")
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
    @Tag(name = "getGameByID", description = "Returns a specific game.")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GameDTO> getSpecificGame(@PathVariable Long id) throws NoSuchElementException {

        Game game = gameService.getSpecificGame(id);
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(mapper.toGameDTO(game),status);
    }

    @PostMapping
    @Tag(name = "postGame", description = "Creates a new game from details provided in the request body. Admin only")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<GameDTO> createNewGame(@RequestBody GameDTOReg gameDTOReg) {
        Game game = mapper.toGame(gameDTOReg);
        Game addedGame = gameService.createNewGame(game);
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(mapper.toGameDTO(addedGame), status);
    }

    @PutMapping("/{id}")
    @Tag(name = "updateGame", description = "Method for updating a game. Admin only")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<GameDTO> updateSpecificGame(@PathVariable Long id, @RequestBody GameDTOUpdate gameDTO) {
        HttpStatus status = HttpStatus.OK;
        Game game = mapper.toGame(gameDTO, id);
        Game updatedGame = gameService.updateSpecificGame(id, game);
        return new ResponseEntity<>(mapper.toGameDTO(updatedGame),status);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    @Tag(name = "deleteGame", description = "Method for deleting a game. Admin only")
    public ResponseEntity<GameDTO> deleteGame(@PathVariable Long id) {
        Game deletedGame = gameService.deleteGame(id);
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(mapper.toGameDTO(deletedGame), status);
    }

    @GetMapping("/{id}/chat")
    @Tag(name = "getGameChat", description = "Method for getting chat for a game. Admins get all messages, " +
            "players get global and faction messages. Optional playerID returns the players messages. " +
            "Optional faction boolean returns only that factions messages")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MessageDTO>> getGameChat(@PathVariable Long id,
                                                     @RequestHeader(required = false) String playerID,
                                                     @RequestHeader(required = false) String human,
                                                     @AuthenticationPrincipal Jwt principal
                                                     ) throws NullPointerException, AppUserNotFoundException {
        System.out.println(principal.getTokenValue());
        HttpStatus status;
        List<Message> messages;
        List<MessageDTO> messageDTOs;
        if(SecurityUtils.isAdmin(principal.getTokenValue())) {
            if (playerID != null) messages = gameService.getGameChat(id, Long.parseLong(playerID));
            else if (human != null) messages = gameService.getGameChat(id, Boolean.valueOf(human));
            else messages = gameService.getGameChat(id);
        }
        else {
            AppUser user = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
            Player player = appUserService.getPlayerByGameAndUser(id, user);
            if (playerID != null && Long.parseLong(playerID) == player.getId()) messages = gameService.getGameChat(id, Long.parseLong(playerID));
            else if (human != null) messages = gameService.getGameChat(id, player.isHuman());
            else messages = gameService.getGameChat(id);
        }
        status = HttpStatus.OK;
        messageDTOs = messages.stream().map(mapper::toMessageDTO).collect(Collectors.toList());

        return new ResponseEntity<>(messageDTOs,status);
    }

    @PostMapping("/{id}/chat")
    @PreAuthorize("isAuthenticated()")
    @Tag(name = "createMessage", description = "Method for posting a new message in game chat.")
    public ResponseEntity<MessageDTO> createNewChat(@PathVariable Long id, @RequestBody MessageDTOreg message, @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException {
        if (SecurityUtils.isAdmin(principal.getTokenValue())) {
            AppUser appUser = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
            Message createdMessage = gameService.createNewAdminChat(id, mapper.toAdminMessage(message), appUser);
            HttpStatus status = HttpStatus.CREATED;
            return new ResponseEntity<>(mapper.toMessageDTO(createdMessage), status);
        } else {
            AppUser appUser = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
            Message createdMessage = gameService.createNewChat(id, mapper.toMessage(message), appUser);
            HttpStatus status = HttpStatus.CREATED;
            return new ResponseEntity<>(mapper.toMessageDTO(createdMessage), status);
        }
    }
}
