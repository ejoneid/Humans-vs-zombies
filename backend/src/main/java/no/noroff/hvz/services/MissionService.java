package no.noroff.hvz.services;

import no.noroff.hvz.models.Game;
import no.noroff.hvz.models.Mission;
import no.noroff.hvz.repositories.GameRepository;
import no.noroff.hvz.repositories.MissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MissionService {

    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private GameRepository gameRepository;

    public List<Mission> getAllMissions(Long gameID) {
        List<Mission> missions = null;
        if(gameRepository.existsById(gameID)) {
            Game game = gameRepository.findById(gameID).get();
            missions = new ArrayList<>(game.getMissions());
        }
        return missions;
    }

    public List<Mission> getAllMissionsFaction(Long gameID, boolean isHuman) {
        List<Mission> missions = null;
        if(gameRepository.existsById(gameID)) {
            Game game = gameRepository.findById(gameID).get();
            missions = game.getMissions().stream().filter(mission -> Objects.equals(mission.isHuman(),isHuman)).collect(Collectors.toList());
        }
        return missions;
    }

    public Mission getSpecificMission(Long gameID, Long missionID) {
        Mission mission = null;
        if(missionRepository.existsById(missionID) && gameRepository.existsById(gameID)) {
            mission = missionRepository.findById(missionID).get();
        }
        return mission;
    }

    public Mission createNewMission(Long gameID, Mission mission) {
        Mission addedMission = new Mission();
        if(gameRepository.existsById(gameID)) {
            addedMission = missionRepository.save(mission);
        }
        return addedMission;
    }

    public Mission updateMission( Long gameID, Long missionID, Mission mission) {
        Mission updatedMission = new Mission();
        if(gameRepository.existsById(gameID) && missionRepository.existsById(missionID)) {
            updatedMission = missionRepository.save(mission);
        }
        return updatedMission;
    }

    public Mission deleteMission(Long gameID, Long missionID) {
        Mission deletedMission = new Mission();
        if(gameRepository.existsById(gameID) && missionRepository.existsById(missionID)) {
            deletedMission = missionRepository.findById(missionID).get();
            missionRepository.deleteById(missionID);
        }
        return deletedMission;
    }
}
