package no.noroff.hvz.controllers;

import no.noroff.hvz.dto.KillDTO;
import no.noroff.hvz.dto.RegKillDTO;
import no.noroff.hvz.mapper.Mapper;
import no.noroff.hvz.models.Kill;
import no.noroff.hvz.services.KillerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/game/{gameID}/kill")
@CrossOrigin(origins = "*")
public class KillController {

    @Autowired
    private KillerService killerService;
    @Autowired
    Mapper mapper;

    @GetMapping
    public ResponseEntity<List<KillDTO>> getAllKills(@PathVariable Long gameID, @RequestHeader(required = false) Long killerID) {
        HttpStatus status;
        List<Kill> kills = new ArrayList<>();
        if (killerID != null) kills = killerService.getAllKills(gameID, killerID);
        else kills = killerService.getAllKills(gameID);
        List<KillDTO> killDTOs = new ArrayList<>();
        if(kills == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.OK;
            killDTOs = kills.stream().map(mapper::toKillTDO).collect(Collectors.toList());
        }
        return new ResponseEntity<>(killDTOs, status);
    }

    @GetMapping("/{killID}")
    public ResponseEntity<KillDTO> getSpecificKill(@PathVariable Long gameID, @PathVariable Long killID) {
        HttpStatus status;
        Kill kill = killerService.getSpecificKill(gameID, killID);
        if(kill.getId() == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(mapper.toKillTDO(kill), status);
    }

    @PostMapping

    public ResponseEntity<KillDTO> createNewKill(@PathVariable Long gameID, @RequestBody RegKillDTO kill) {
        HttpStatus status;
        Kill addedKill = killerService.createNewKill(gameID, mapper.regKillDTO(kill));
        if(addedKill == null) {
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(null, status);
        }
        else {
            status = HttpStatus.CREATED;
            return new ResponseEntity<>(mapper.toKillTDO(addedKill), status);
        }
    }

    @PutMapping("/{killID}")
    public ResponseEntity<KillDTO> updateKill(@PathVariable Long gameID, @PathVariable Long killID, @RequestBody Kill kill) {
        HttpStatus status;
        if(!Objects.equals(killID,kill.getId())) {
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(null, status);
        }
        Kill updatedKill = killerService.updateKill(gameID, killID, kill);
        if(updatedKill.getId() == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(mapper.toKillTDO(updatedKill), status);
    }

    @DeleteMapping("/{killID}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<KillDTO> deleteKill(@PathVariable Long gameID, @PathVariable Long killID) {
        HttpStatus status;
        Kill deletedKill = killerService.deleteKill(gameID, killID);
        if (deletedKill.getId() == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(mapper.toKillTDO(deletedKill), status);
    }
}
