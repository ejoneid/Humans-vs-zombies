package no.noroff.hvz.controllers;

import no.noroff.hvz.dto.MissionDTO;
import no.noroff.hvz.mapper.Mapper;
import no.noroff.hvz.models.Mission;
import no.noroff.hvz.models.Player;
import no.noroff.hvz.security.SecurityUtils;
import no.noroff.hvz.services.MissionService;
import no.noroff.hvz.services.PlayerService;
import no.noroff.hvz.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/game/{gameID}/mission")
@CrossOrigin(origins = "*")
public class MissionController {

    @Autowired
    private MissionService missionService;
    @Autowired
    private Mapper mapper;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private PlayerService playerService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MissionDTO>> getAllMissions(@PathVariable Long gameID,@RequestHeader String authorization, @AuthenticationPrincipal Jwt principal) {
        HttpStatus status;
        try {
            Player player = playerService.getPlayerByGameAndUser(gameID, appUserService.getSpecificUser(principal.getClaimAsString("sub")));
            List<MissionDTO> missionDTOs;
            List<Mission> missions;
            if(SecurityUtils.isAdmin(authorization)) {
                missions = missionService.getAllMissions(gameID);
            }
            else {
                missions = missionService.getAllMissionsFaction(gameID, player.isHuman());
            }
            missionDTOs = missions.stream().map(mapper::toMissionDTO).collect(Collectors.toList());
            status = HttpStatus.OK;
            return new ResponseEntity<>(missionDTOs, status);
        }
        catch(NullPointerException e) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(null, status);
        }
    }

    @GetMapping("/{missionID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MissionDTO> getSpecificMission(@PathVariable Long gameID, @PathVariable Long missionID, @RequestHeader String authorization, @AuthenticationPrincipal Jwt principal) {
        HttpStatus status;
        try {
            Player player = playerService.getPlayerByGameAndUser(gameID, appUserService.getSpecificUser(principal.getClaimAsString("sub")));
            Mission mission = missionService.getSpecificMission(gameID,missionID);
            if(!SecurityUtils.isAdmin(authorization)) {
                if(mission.isHuman() != player.isHuman()) {
                    status = HttpStatus.FORBIDDEN;
                    return new ResponseEntity<>(null, status);
                }
            }
            status = HttpStatus.OK;
            return new ResponseEntity<>(mapper.toMissionDTO(mission),status);
        }
        catch(NullPointerException e) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(null, status);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<MissionDTO> createNewMission(@PathVariable Long gameID, @RequestBody Mission mission) {
        HttpStatus status;
        Mission addedMission = missionService.createNewMission(gameID, mission);
        if(addedMission.getId() == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.CREATED;
        }
        return new ResponseEntity<>(mapper.toMissionDTO(addedMission), status);
    }

    @PutMapping("/{missionID}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<MissionDTO> updateMission(@PathVariable Long gameID, @PathVariable Long missionID, @RequestBody Mission mission) {
        HttpStatus status;
        if(!Objects.equals(missionID,mission.getId())) {
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(mapper.toMissionDTO(new Mission()),status);
        }
        Mission updatedMission = missionService.updateMission(gameID, missionID, mission);
        if(updatedMission.getId() == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(mapper.toMissionDTO(updatedMission),status);
    }

    @DeleteMapping("/{missionID}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<MissionDTO> deleteMission(@PathVariable Long gameID, @PathVariable Long missionID) {
        HttpStatus status;
        Mission deletedMission = missionService.deleteMission(gameID, missionID);
        if(deletedMission.getId() == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(mapper.toMissionDTO(deletedMission), status);
    }
}
