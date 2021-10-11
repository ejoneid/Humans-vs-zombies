package no.noroff.hvz.controllers;

import no.noroff.hvz.dto.MissionDTO;
import no.noroff.hvz.mapper.Mapper;
import no.noroff.hvz.models.Game;
import no.noroff.hvz.models.Mission;
import no.noroff.hvz.services.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping
    public ResponseEntity<List<MissionDTO>> getAllMissions(@PathVariable Long gameID) {
        HttpStatus status;
        List<Mission> missions = missionService.getAllMissions(gameID);
        List<MissionDTO> missionDTOs = new ArrayList<>();
        if(missions == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.OK;
            missionDTOs = missions.stream().map(mapper::toMissionDTO).collect(Collectors.toList());
        }
        return new ResponseEntity<>(missionDTOs, status);
    }

    @GetMapping("/{missionID}")
    public ResponseEntity<MissionDTO> getSpecificMission(@PathVariable Long gameID, @PathVariable Long missionID) {
        HttpStatus status;
        Mission mission = missionService.getSpecificMission(gameID,missionID);
        if(mission.getId() == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(mapper.toMissionDTO(mission),status);
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
