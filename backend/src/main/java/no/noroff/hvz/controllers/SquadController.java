package no.noroff.hvz.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import no.noroff.hvz.dto.message.MessageDTO;
import no.noroff.hvz.dto.message.MessageDTOreg;
import no.noroff.hvz.dto.squad.*;
import no.noroff.hvz.exceptions.AppUserNotFoundException;
import no.noroff.hvz.exceptions.MissingPermissionsException;
import no.noroff.hvz.exceptions.MissingPlayerException;
import no.noroff.hvz.mapper.Mapper;
import no.noroff.hvz.models.*;
import no.noroff.hvz.security.SecurityUtils;
import no.noroff.hvz.services.SquadService;
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
@RequestMapping("/api/game/{gameID}/squad")
@CrossOrigin(origins = "*")
public class SquadController {

    @Autowired
    private SquadService squadService;
    @Autowired
    private Mapper mapper;
    @Autowired
    private AppUserService appUserService;

    private HttpStatus status;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Tag(name = "getAllSquads", description = "Method for getting all squads in a game. Optional player id returns that players squad instead.")
    public ResponseEntity<List<SquadDTO>> getAllSquads(@PathVariable Long gameID, @RequestParam Optional<String> playerId) {
        List<SquadDTO> squadDTOs = new ArrayList<>();
        List<Squad> squads;
        if (playerId.isPresent()) {
            Squad squad = squadService.getSquadByPlayer(gameID, Long.parseLong(playerId.get()));
            SquadDTO squadDTO = mapper.toSquadDTO(squad);
            squadDTOs.add(squadDTO);
        }
        else {
            squads = squadService.getAllSquads(gameID);
            squadDTOs = squads.stream().map(mapper::toSquadDTO).collect(Collectors.toList());
        }

        return new ResponseEntity<>(squadDTOs,HttpStatus.OK);
    }

    @GetMapping("/{squadID}")
    @PreAuthorize("isAuthenticated()")
    @Tag(name = "getSpecificSquad", description = "Method for getting a squad in a game.")
    public ResponseEntity<SquadDTO> getSpecificSquad(@PathVariable Long gameID, @PathVariable Long squadID) {
        Squad squad = squadService.getSpecificSquad(gameID, squadID);
        SquadDTO squadDTO = mapper.toSquadDTO(squad);
        status = HttpStatus.OK;
        return new ResponseEntity<>(squadDTO, status);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Tag(name = "createSquad", description = "Method for creating a squad in a game. If created by a player, thet player is added as first member")
    public ResponseEntity<SquadDTO> createNewSquad(@PathVariable Long gameID, @RequestBody SquadDTOReg squad,
                                                   @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException, MissingPermissionsException {
        AppUser user = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
        Squad createdSquad;

        if (SecurityUtils.isAdmin(principal.getTokenValue())) {
            createdSquad = squadService.createNewSquad(gameID, mapper.toSquad(squad));
        }
        else {
            Player player;
            // Denne try-catch'en må være her ettersom det ikke skal returne 404 som er standard for NoSuchElementException.
            try {
                player = appUserService.getPlayerByGameAndUser(gameID, user);
            } catch (MissingPlayerException e) {
                throw new MissingPermissionsException("User is not a player in this game");
            }
            createdSquad = squadService.createNewSquad(gameID, mapper.toSquad(squad), player);
        }

        status = HttpStatus.CREATED;
        SquadDTO squadDTO = mapper.toSquadDTO(createdSquad);
        return new ResponseEntity<>(squadDTO, status);
    }

    @PostMapping("/{squadID}/join")
    @PreAuthorize("isAuthenticated()")
    @Tag(name = "joinSquad", description = "Method for joining a squad in a game.")
    public ResponseEntity<SquadDTO> joinSquad(@PathVariable Long gameID, @PathVariable Long squadID, @RequestBody SquadMemberFromDTO member) {
        SquadMember addedSquadMember = squadService.joinSquad(gameID, squadID, mapper.toSquadMember(member, gameID));
        SquadDTO squadDTO = mapper.toSquadDTO(squadService.getSpecificSquad(gameID, addedSquadMember.getSquad().getId()));
        status = HttpStatus.CREATED;
        return new ResponseEntity<>(squadDTO, status);
    }

    @PutMapping("/{squadID}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    @Tag(name = "updateSquad", description = "Method for updating a squad in a game. Admin only")
    public ResponseEntity<SquadDTO> updateSquad(@PathVariable Long gameID, @PathVariable Long squadID, @RequestBody SquadDTOUpdate squadDTO) {
        Squad updatedSquad = squadService.updateSquad(gameID, squadID, squadDTO);
        status = HttpStatus.OK;
        return new ResponseEntity<>(mapper.toSquadDTO(updatedSquad), status);
    }

    @DeleteMapping("/{squadID}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    @Tag(name = "deleteSquad", description = "Method for deleting a squad in a game. Admin only")
    public ResponseEntity<SquadDTO> deleteSquad(@PathVariable Long gameID, @PathVariable Long squadID) {
        Squad deletedSquad = squadService.deleteSquad(gameID, squadID);
        status = HttpStatus.OK;
        SquadDTO squadDTO = mapper.toSquadDTO(deletedSquad);
        return new ResponseEntity<>(squadDTO, status);
    }

    @GetMapping("/{squadID}/chat")
    @PreAuthorize("isAuthenticated()")
    @Tag(name = "getSquadChat", description = "Method for getting the chat in a squad in a game. Players will only get messages if they are in the squad and matches the squads faction.")
    public ResponseEntity<List<MessageDTO>> getSquadChat(@PathVariable Long gameID, @PathVariable Long squadID,
                                                         @RequestHeader String authorization, @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException, MissingPermissionsException {
        List<MessageDTO> chatDTO;
        List<Message> chat = squadService.getSquadChat(gameID, squadID);

        if(SecurityUtils.isAdmin(authorization)) {
            chatDTO = chat.stream().map(mapper::toMessageDTO).collect(Collectors.toList());
            status = HttpStatus.OK;
        }
        else {
            AppUser appUser = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
            Player player = appUserService.getPlayerByGameAndUser(gameID, appUser);
            Squad squad= squadService.getSpecificSquad(gameID, squadID);
            //check if player
            if(player.isHuman() == squad.isHuman() && squadService.isMemberOfSquad(squad,player)) {
                chatDTO = chat.stream().map(mapper::toMessageDTO).collect(Collectors.toList());
                status = HttpStatus.OK;
            }
            else {
                throw new MissingPermissionsException("User do not have the right permissions for this operation.");
            }
        }
        return new ResponseEntity<>(chatDTO, status);
    }

    @PostMapping("/{squadID}/chat")
    @PreAuthorize("isAuthenticated()")
    @Tag(name = "createSquadChat", description = "Method for creating a new message in the squad chat. Players must be part of the squad and the correct faction.")
    public ResponseEntity<MessageDTO> createSquadChat(@PathVariable Long gameID, @PathVariable Long squadID, @RequestBody MessageDTOreg message, @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException, MissingPermissionsException {
        AppUser appUser = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
        if(!SecurityUtils.isAdmin(principal.getTokenValue())) {
            try {
                Player player = appUserService.getPlayerByGameAndUser(gameID, appUser);
                Squad squad= squadService.getSpecificSquad(gameID, squadID);
                if(player.isHuman() != squad.isHuman() || !squadService.isMemberOfSquad(squad,player)) {
                    throw new MissingPermissionsException("Player is not allowed to post messages in this squad");
                }
            } catch (NoSuchElementException e) {
                throw new MissingPermissionsException("User is not a member of this squad.");
            }
        }
        Message chat = squadService.createSquadChat(gameID, squadID, appUser, mapper.toMessage(message));
        status = HttpStatus.CREATED;
        MessageDTO messageDTO = mapper.toMessageDTO(chat);
        return new ResponseEntity<>(messageDTO, status);
    }

    @GetMapping("/{squadID}/check-in")
    @PreAuthorize("isAuthenticated()")
    @Tag(name = "getAllSquadCheckIns", description = "Method for getting all squadCheckIns for a squad in a game. Players must be part of the squad and the correct faction.")
    public ResponseEntity<List<SquadCheckInDTO>> getSquadCheckIn(@PathVariable Long gameID, @PathVariable Long squadID,
                                                                 @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException, MissingPermissionsException {
        List<SquadCheckInDTO> checkInDTOs;
        List<SquadCheckIn> checkins = squadService.getSquadCheckIn(gameID, squadID);
        if(SecurityUtils.isAdmin(principal.getTokenValue())) {
            checkInDTOs = checkins.stream().map(mapper::toSquadCheckInDTO).collect(Collectors.toList());
            status = HttpStatus.OK;
        }
        else {
            AppUser appUser = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
            Player player = appUserService.getPlayerByGameAndUser(gameID, appUser);
            Squad squad= squadService.getSpecificSquad(gameID, squadID);
            //check if player
            if(player.isHuman() == squad.isHuman() && squadService.isMemberOfSquad(squad,player)) {
                checkInDTOs = checkins.stream().map(mapper::toSquadCheckInDTO).collect(Collectors.toList());
                status = HttpStatus.OK;
            }
            else {
                throw new MissingPermissionsException("User is not a member of this squad.");
            }
        }
        return new ResponseEntity<>(checkInDTOs, status);
    }

    @PostMapping("/{squadID}/check-in")
    @PreAuthorize("isAuthenticated()")
    @Tag(name = "createSquadCheckIn", description = "Method for creating a squadCheckIn for a squad in a game. User must be part of the squad and the correct faction.")
    public ResponseEntity<SquadCheckInDTO> createSquadCheckIn(@PathVariable Long gameID, @PathVariable Long squadID,
                                                              @RequestBody SquadCheckInDTOReg checkInDTO, @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException, MissingPermissionsException {
        SquadCheckInDTO addedCheckInDTO;
        AppUser appUser = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
        Player player = appUserService.getPlayerByGameAndUser(gameID, appUser);
        Squad squad= squadService.getSpecificSquad(gameID, squadID);
        //check if player and squad is same faction or admin
        if((player.isHuman() == squad.isHuman() && squadService.isMemberOfSquad(squad,player)) ||
                SecurityUtils.isAdmin(principal.getTokenValue())) {
            SquadCheckIn addedCheckIn = squadService.createSquadCheckIn(gameID, squadID, mapper.toSquadCheckIn(checkInDTO, gameID));
            status = HttpStatus.OK;
            addedCheckInDTO = mapper.toSquadCheckInDTO(addedCheckIn);
        }
        else {
            throw new MissingPermissionsException("User is not a member of this squad.");
        }
        return new ResponseEntity<>(addedCheckInDTO, status);
    }

    @DeleteMapping("/{squadID}/leave")
    @PreAuthorize("isAuthenticated()")
    @Tag(name = "leaveSquad", description = "Method for leaving a squad in a game.")
    public ResponseEntity<SquadMemberDTO> leaveSquad(@PathVariable Long gameID, @PathVariable Long squadID, @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException {
        AppUser appUser = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
        Player player = appUserService.getPlayerByGameAndUser(gameID, appUser);

        return new ResponseEntity<>(null, status);
    }
}
