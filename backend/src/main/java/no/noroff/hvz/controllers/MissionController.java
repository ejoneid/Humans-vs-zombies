package no.noroff.hvz.controllers;

import no.noroff.hvz.dto.mission.MissionDTO;
import no.noroff.hvz.dto.mission.MissionDTOReg;
import no.noroff.hvz.exceptions.AppUserNotFoundException;
import no.noroff.hvz.exceptions.MissingPermissionsException;
import no.noroff.hvz.mapper.Mapper;
import no.noroff.hvz.models.AppUser;
import no.noroff.hvz.models.Game;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
    public ResponseEntity<List<MissionDTO>> getAllMissions(@PathVariable Long gameID,@RequestHeader String authorization, @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException {
        HttpStatus status;
        List<MissionDTO> missionDTOs;
        List<Mission> missions;
        if(SecurityUtils.isAdmin(authorization)) {
            missions = missionService.getAllMissions(gameID);
        }
        else {
            Player player = playerService.getPlayerByGameAndUser(gameID, appUserService.getSpecificUser(principal.getClaimAsString("sub")));
            missions = missionService.getAllMissionsFaction(gameID, player.isHuman());
        }
        missionDTOs = missions.stream().map(mapper::toMissionDTO).collect(Collectors.toList());
        status = HttpStatus.OK;
        return new ResponseEntity<>(missionDTOs, status);
    }

    @GetMapping("/{missionID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MissionDTO> getSpecificMission(@PathVariable Long gameID, @PathVariable Long missionID, @RequestHeader String authorization, @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException, MissingPermissionsException {
        Mission mission = missionService.getSpecificMission(gameID,missionID);
        if(!SecurityUtils.isAdmin(authorization)) {
            Player player = playerService.getPlayerByGameAndUser(gameID, appUserService.getSpecificUser(principal.getClaimAsString("sub")));
            if(mission.isHuman() != player.isHuman()) {
                throw new MissingPermissionsException("User does not have the right permissions for this operation");
            }
        }
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(mapper.toMissionDTO(mission),status);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<MissionDTO> createNewMission(@PathVariable Long gameID, @RequestBody MissionDTOReg missionDTO) {
        Mission mission = mapper.toMission(missionDTO, gameID);
        mission = missionService.createNewMission(gameID, mission);
        MissionDTO addedMissionDTO = mapper.toMissionDTO(mission);
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(addedMissionDTO, status);
    }

    @PutMapping("/{missionID}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<MissionDTO> updateMission(@PathVariable Long gameID, @PathVariable Long missionID, @RequestBody MissionDTOReg missionDTO) {
        Mission mission = mapper.toMission(missionDTO, gameID);
        Mission updatedMission = missionService.updateMission(gameID, missionID, mission);
        MissionDTO updatedMissionDTO = mapper.toMissionDTO(updatedMission);
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(updatedMissionDTO, status);
    }

    @DeleteMapping("/{missionID}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<MissionDTO> deleteMission(@PathVariable Long gameID, @PathVariable Long missionID) {
        Mission deletedMission = missionService.deleteMission(gameID, missionID);
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(mapper.toMissionDTO(deletedMission), status);
    }
}
