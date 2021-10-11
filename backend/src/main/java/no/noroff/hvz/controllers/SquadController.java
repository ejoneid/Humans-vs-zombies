package no.noroff.hvz.controllers;

import no.noroff.hvz.dto.*;
import no.noroff.hvz.mapper.Mapper;
import no.noroff.hvz.models.*;
import no.noroff.hvz.services.SquadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/game/{gameID}/squad")
@CrossOrigin(origins = "*")
public class SquadController {

    @Autowired
    private SquadService squadService;
    @Autowired
    private Mapper mapper;

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
    public ResponseEntity<SquadDTO> createNewSquad(@PathVariable Long gameID, @RequestBody Squad squad) {

        Squad createdSquad = squadService.createNewSquad(gameID, squad);
        SquadDTO squadDTO = null;
        if(createdSquad == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.CREATED;
            squadDTO = mapper.toSquadDTO(squad);
        }
        return new ResponseEntity<>(squadDTO, status);
    }

    @PostMapping("/{squadID}/join")
    public ResponseEntity<SquadMemberDTO> joinSquad(@PathVariable Long gameID, @PathVariable Long squadID, @RequestBody SquadMemberFromDTO member) {
        SquadMember addedSquadMember = squadService.joinSquad(gameID, squadID, mapper.toSquadMember(member));
        SquadMemberDTO squadMemberDTO = null;
        if(addedSquadMember == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.CREATED;
            squadMemberDTO = mapper.toSquadMemberDTO(addedSquadMember);
        }
        return new ResponseEntity<>(squadMemberDTO, status);
    }

    @PutMapping("/{squadID}")
//    @PreAuthorize("hasRole('ADMIN')")
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
//    @PreAuthorize("hasRole('ADMIN')")
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
    public ResponseEntity<List<MessageDTO>> getSquadChat(@PathVariable Long gameID, @PathVariable Long squadID) {
        List<Message> chat = squadService.getSquadChat(gameID, squadID);
        List<MessageDTO> chatDTO = null;
        if(chat == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.OK;
            chatDTO = chat.stream().map(mapper::toMessageDTO).collect(Collectors.toList());
        }
        return new ResponseEntity<>(chatDTO, status);
    }

    @PostMapping("/{squadID}/chat")
    public ResponseEntity<MessageDTO> createSquadChat(@PathVariable Long gameID, @PathVariable Long squadID, @RequestHeader Long playerID, @RequestBody Message message) {
        Message chat = squadService.createSquadChat(gameID, squadID, playerID, message);
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
    public ResponseEntity<List<SquadCheckInDTO>> getSquadCheckIn(@PathVariable Long gameID, @PathVariable Long squadID) {
        List<SquadCheckIn> checkins = squadService.getSquadCheckIn(gameID, squadID);
        List<SquadCheckInDTO> checkInsDTO = null;
        if(checkins == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.OK;
            checkInsDTO = checkins.stream().map(mapper::toSquadCheckInDTO).collect(Collectors.toList());
        }

        return new ResponseEntity<>(checkInsDTO, status);
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
