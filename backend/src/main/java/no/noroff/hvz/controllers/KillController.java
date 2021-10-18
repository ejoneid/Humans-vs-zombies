package no.noroff.hvz.controllers;

import no.noroff.hvz.dto.kill.KillDTO;
import no.noroff.hvz.dto.kill.KillDTOReg;
import no.noroff.hvz.exceptions.AppUserNotFoundException;
import no.noroff.hvz.exceptions.InvalidBiteCodeException;
import no.noroff.hvz.exceptions.MissingPermissionsException;
import no.noroff.hvz.mapper.Mapper;
import no.noroff.hvz.models.Kill;
import no.noroff.hvz.security.SecurityUtils;
import no.noroff.hvz.services.KillerService;
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
@RequestMapping("/api/game/{gameID}/kill")
@CrossOrigin(origins = "*")
public class KillController {

    @Autowired
    private KillerService killerService;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    Mapper mapper;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<KillDTO>> getAllKills(@PathVariable Long gameID, @RequestParam(required = false) Long killerID) {
        List<Kill> kills;
        if (killerID != null) kills = killerService.getAllKills(gameID, killerID);
        else kills = killerService.getAllKills(gameID);
        HttpStatus status = HttpStatus.OK;
        List<KillDTO> killDTOs = kills.stream().map(mapper::toKillDTO).collect(Collectors.toList());
        return new ResponseEntity<>(killDTOs, status);
    }

    @GetMapping("/{killID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<KillDTO> getSpecificKill(@PathVariable Long gameID, @PathVariable Long killID) {
        Kill kill = killerService.getSpecificKill(gameID, killID);
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(mapper.toKillDTO(kill), status);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<KillDTO> createNewKill(@PathVariable Long gameID, @RequestBody KillDTOReg kill) throws InvalidBiteCodeException {
        Kill addedKill = killerService.createNewKill(gameID, mapper.regKillDTO(kill));
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(mapper.toKillDTO(addedKill), status);
    }

    @PutMapping("/{killID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<KillDTO> updateKill(@PathVariable Long gameID, @PathVariable Long killID, @RequestBody KillDTOReg kill, @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException, MissingPermissionsException {
        Kill unchangedKill = killerService.getSpecificKill(gameID, killID);
        String userOpenId = principal.getClaimAsString("sub");
        // Checks if the user is authorized as admin or the killer
        if (!(SecurityUtils.isAdmin(principal.getTokenValue()) || unchangedKill.getKiller().getUser().equals( appUserService.getSpecificUser(userOpenId) ))) {
            throw new MissingPermissionsException("User does not have the right permissions for this operation");
        }
        Kill updatedKill = killerService.updateKill(gameID, killID, kill);
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(mapper.toKillDTO(updatedKill), status);
    }

    @DeleteMapping("/{killID}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<KillDTO> deleteKill(@PathVariable Long gameID, @PathVariable Long killID) {
        Kill deletedKill = killerService.deleteKill(gameID, killID);
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(mapper.toKillDTO(deletedKill), status);
    }
}
