package no.noroff.hvz.controllers;

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
    public ResponseEntity<SquadDTO> getSpecificSquad(@PathVariable Long gameID, @PathVariable Long squadID) {
        Squad squad = squadService.getSpecificSquad(gameID, squadID);
        SquadDTO squadDTO = mapper.toSquadDTO(squad);
        status = HttpStatus.OK;
        return new ResponseEntity<>(squadDTO, status);
    }

    @PostMapping
    public ResponseEntity<SquadDTO> createNewSquad(@PathVariable Long gameID, @RequestBody SquadJoinDTO squad,
                                                   @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException, MissingPermissionsException {
        AppUser user = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
        Squad createdSquad;

        if (SecurityUtils.isAdmin(principal.getTokenValue())) {
            createdSquad = squadService.createNewSquad(gameID, mapper.joinToSquad(squad));
            createdSquad.setMembers(new HashSet<>());
        }
        else {
            Player player;
            // Denne try-catch'en må være her ettersom det ikke skal returne 404 som er standard for NoSuchElementException.
            try {
                player = appUserService.getPlayerByGameAndUser(gameID, user);
            } catch (MissingPlayerException e) {
                throw new MissingPermissionsException("User is not a player in this game");
            }
            createdSquad = squadService.createNewSquad(gameID, mapper.joinToSquad(squad));
            createdSquad.setHuman(player.isHuman());
            createdSquad.setMembers(new HashSet<>());
            SquadMember member = new SquadMember();
            member.setPlayer(player);
            squadService.joinSquad(gameID, createdSquad.getId(), member);
            //Gets the updated squad with the new member
            createdSquad = squadService.getSpecificSquad(gameID,member.getSquad().getId());
        }

        status = HttpStatus.CREATED;
        SquadDTO squadDTO = mapper.toSquadDTO(createdSquad);
        return new ResponseEntity<>(squadDTO, status);
    }

    @PostMapping("/{squadID}/join")
    public ResponseEntity<SquadDTO> joinSquad(@PathVariable Long gameID, @PathVariable Long squadID, @RequestBody SquadMemberFromDTO member) {
        SquadMember addedSquadMember = squadService.joinSquad(gameID, squadID, mapper.toSquadMember(member, gameID));
        SquadDTO squadDTO = mapper.toSquadDTO(squadService.getSpecificSquad(gameID, addedSquadMember.getSquad().getId()));
        status = HttpStatus.CREATED;

        return new ResponseEntity<>(squadDTO, status);
    }

    @PutMapping("/{squadID}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<SquadDTO> updateSquad(@PathVariable Long gameID, @PathVariable Long squadID, @RequestBody SquadDTOUpdate squadDTO) {
        Squad updatedSquad = squadService.updateSquad(gameID, squadID, squadDTO);
        status = HttpStatus.OK;
        return new ResponseEntity<>(mapper.toSquadDTO(updatedSquad), status);
    }

    @DeleteMapping("/{squadID}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<SquadDTO> deleteSquad(@PathVariable Long gameID, @PathVariable Long squadID) {
        Squad deletedSquad = squadService.deleteSquad(gameID, squadID);
        status = HttpStatus.OK;
        SquadDTO squadDTO = mapper.toSquadDTO(deletedSquad);
        return new ResponseEntity<>(squadDTO, status);
    }

    @GetMapping("/{squadID}/chat")
    public ResponseEntity<List<MessageDTO>> getSquadChat(@PathVariable Long gameID, @PathVariable Long squadID,
                                                         @RequestHeader String authorization, @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException, MissingPermissionsException {
        List<MessageDTO> chatDTO = new ArrayList<>();
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
    public ResponseEntity<MessageDTO> createSquadChat(@PathVariable Long gameID, @PathVariable Long squadID, @RequestBody MessageDTOreg message, @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException, MissingPermissionsException {
        AppUser appUser = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
        try {
            appUserService.getPlayerByGameAndUser(gameID, appUser);
        } catch (NoSuchElementException e) {
            throw new MissingPermissionsException("User is not a member of this squad.");
        }
        Message chat = squadService.createSquadChat(gameID, squadID, appUser, mapper.toMessage(message));
        status = HttpStatus.CREATED;
        MessageDTO messageDTO = mapper.toMessageDTO(chat);
        return new ResponseEntity<>(messageDTO, status);
    }

    @GetMapping("/{squadID}/check-in")
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
    public ResponseEntity<SquadCheckInDTO> createSquadCheckIn(@PathVariable Long gameID, @PathVariable Long squadID,
                                                              @RequestBody SquadCheckInDTO checkInDTO, @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException, MissingPermissionsException {
        SquadCheckInDTO addedCheckInDTO;
        AppUser appUser = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
        Player player = appUserService.getPlayerByGameAndUser(gameID, appUser);
        Squad squad= squadService.getSpecificSquad(gameID, squadID);
        //check if player and squad is same faction
        if(player.isHuman() == squad.isHuman() && squadService.isMemberOfSquad(squad,player)) {
            SquadCheckIn addedCheckIn = squadService.createSquadCheckIn(gameID, squadID, mapper.toSquadCheckIn(checkInDTO, gameID));
            status = HttpStatus.OK;
            addedCheckInDTO = mapper.toSquadCheckInDTO(addedCheckIn);
        }
        else {
            throw new MissingPermissionsException("User is not a member of this squad.");
        }
        return new ResponseEntity<>(addedCheckInDTO, status);
    }
}
