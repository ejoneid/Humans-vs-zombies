package no.noroff.hvz.controllers;

import no.noroff.hvz.dto.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    public ResponseEntity<List<SquadDTO>> getAllSquads(@PathVariable Long gameID) {
        List<Squad> squads = squadService.getAllSquads(gameID);
        List<SquadDTO> squadDTOs = null;
        if(squads == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.OK;
            squadDTOs = squads.stream().map(mapper::toSquadDTO).collect(Collectors.toList());
        }
        return new ResponseEntity<>(squadDTOs,status);
    }

    @GetMapping("/{squadID}")
    public ResponseEntity<SquadDTO> getSpecificSquad(@PathVariable Long gameID, @PathVariable Long squadID) {
        Squad squad = squadService.getSpecificSquad(gameID, squadID);
        SquadDTO squadDTO= null;
        if (squad== null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.OK;
            squadDTO = mapper.toSquadDTO(squad);
        }

        return new ResponseEntity<>(squadDTO, status);
    }

    @PostMapping
    public ResponseEntity<SquadDTO> createNewSquad(@PathVariable Long gameID, @RequestBody Squad squad,
                                                   @AuthenticationPrincipal Jwt principal) {
        SquadDTO squadDTO = null;
        try {
            AppUser user = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
            Player player = appUserService.getPlayerByGameAndUser(gameID, user);
            Squad createdSquad = squadService.createNewSquad(gameID, squad);
            SquadMember member = new SquadMember();
            member.setRank(1);
            member.setPlayer(player);
            member = squadService.joinSquad(gameID, createdSquad.getId(), member);
            status = HttpStatus.CREATED;
            //Gets the updated squad with the new member
            createdSquad = squadService.getSpecificSquad(gameID,member.getSquad().getId());
            squadDTO = mapper.toSquadDTO(createdSquad);
        }
        catch (NullPointerException e) {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(squadDTO, status);
    }

    @PostMapping("/{squadID}/join")
    public ResponseEntity<SquadDTO> joinSquad(@PathVariable Long gameID, @PathVariable Long squadID, @RequestBody SquadMemberFromDTO member) {
        SquadDTO squadDTO = null;
        try {
            SquadMember addedSquadMember = squadService.joinSquad(gameID, squadID, mapper.toSquadMember(member));
            squadDTO = mapper.toSquadDTO(squadService.getSpecificSquad(gameID, addedSquadMember.getSquad().getId()));
            status = HttpStatus.CREATED;
        }
        catch (NullPointerException e) {
            status = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(squadDTO, status);
    }

    @PutMapping("/{squadID}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<SquadDTO> updateSquad(@PathVariable Long gameID, @PathVariable Long squadID, @RequestBody Squad squad) {
        SquadDTO squadDTO = null;
        if(!Objects.equals(squadID, squad.getId())) {
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(squadDTO,status);
        }
        Squad updatedSquad = squadService.updateSquad(gameID, squadID, squad);
        if(updatedSquad == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.OK;
            squadDTO = mapper.toSquadDTO(updatedSquad);
        }
        return new ResponseEntity<>(squadDTO,status);
    }

    @DeleteMapping("/{squadID}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<SquadDTO> deleteSquad(@PathVariable Long gameID, @PathVariable Long squadID) {
        Squad deletedSquad = squadService.deleteSquad(gameID, squadID);
        SquadDTO squadDTO = null;
        if(deletedSquad == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.OK;
            squadDTO = mapper.toSquadDTO(deletedSquad);
        }

        return new ResponseEntity<>(squadDTO, status);
    }

    @GetMapping("/{squadID}/chat")
    public ResponseEntity<List<MessageDTO>> getSquadChat(@PathVariable Long gameID, @PathVariable Long squadID,
                                                         @RequestHeader String authorization, @AuthenticationPrincipal Jwt principal) {
        List<MessageDTO> chatDTO = new ArrayList<>();
        try{
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
                    status = HttpStatus.FORBIDDEN;
                }
            }
        }
        catch (NullPointerException e) {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(chatDTO, status);
    }

    @PostMapping("/{squadID}/chat")
    public ResponseEntity<MessageDTO> createSquadChat(@PathVariable Long gameID, @PathVariable Long squadID, @RequestHeader String playerID, @RequestBody Message message) {
        //TODO skal ta inn MessageDTO, du kan endre den hvis du trenger at den gjør noe annet, eventuelt lage en reg versjon
        Message chat = squadService.createSquadChat(gameID, squadID, Long.parseLong(playerID), message);
        MessageDTO messageDTO = null;
        if(chat == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.CREATED;
            messageDTO = mapper.toMessageDTO(chat);
        }
        return new ResponseEntity<>(messageDTO, status);
    }

    @GetMapping("/{squadID}/check-in")
    public ResponseEntity<List<SquadCheckInDTO>> getSquadCheckIn(@PathVariable Long gameID, @PathVariable Long squadID,
                                                                 @RequestHeader String authorization, @AuthenticationPrincipal Jwt principal) {
        List<SquadCheckInDTO> checkInDTOs = new ArrayList<>();
        try{
            List<SquadCheckIn> checkins = squadService.getSquadCheckIn(gameID, squadID);
            if(SecurityUtils.isAdmin(authorization)) {
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
                    status = HttpStatus.FORBIDDEN;
                }
            }
        }
        catch (NullPointerException e) {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(checkInDTOs, status);
    }

    @PostMapping("/{squadID}/check-in")
    public ResponseEntity<SquadCheckInDTO> createSquadCheckIn(@PathVariable Long gameID, @PathVariable Long squadID, @RequestBody SquadCheckIn checkin) {
        SquadCheckIn addedCheckIn = squadService.createSquadCheckIn(gameID, squadID, checkin);
        SquadCheckInDTO checkInDTO = null;
        if(addedCheckIn == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else{
            status = HttpStatus.CREATED;
            checkInDTO = mapper.toSquadCheckInDTO(addedCheckIn);
        }
        return new ResponseEntity<>(checkInDTO, status);
    }
}
