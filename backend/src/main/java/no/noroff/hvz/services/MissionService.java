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

@Service
public class MissionService {

    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private GameRepository gameRepository;

    public ResponseEntity<List<Mission>> getAllMissions(Long gameID) {
        HttpStatus status;
        List<Mission> missions = new ArrayList<>();
        if(!gameRepository.existsById(gameID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(missions,status);
        }
        Game game = gameRepository.findById(gameID).get();
        missions = new ArrayList<>(game.getMissions());
        status = HttpStatus.OK;
        return new ResponseEntity<>(missions, status);
    }

    public ResponseEntity<Mission> getSpecificMission(Long gameID, Long missionID) {
        HttpStatus status;
        Mission mission = new Mission();
        if(!missionRepository.existsById(missionID) || !gameRepository.existsById(gameID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(mission,status);
        }
        mission = missionRepository.findById(missionID).get();
        status = HttpStatus.OK;
        return new ResponseEntity<>(mission,status);
    }

    public ResponseEntity<Mission> createNewMission(Long gameID, Mission mission) {
        HttpStatus status;
        Mission addedMission = new Mission();
        if(!gameRepository.existsById(gameID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(mission,status);
        }
        //adds new mission
        addedMission = missionRepository.save(mission);
        //Updates missions in specified game, tror ikke dette trengs
        /*Game game = gameRepository.findById(gameID).get();
        Set<Mission> missions = game.getMissions();
        missions.add(addedMission);
        game.setMissions(missions);
        gameRepository.save(game);

         */
        //returns new mission with code
        status = HttpStatus.CREATED;
        return new ResponseEntity<>(addedMission, status);
    }

    public ResponseEntity<Mission> updateMission( Long gameID, Long missionID, Mission mission) {
        HttpStatus status;
        Mission updatedMission = new Mission();
        if(!gameRepository.existsById(gameID) || !missionRepository.existsById(missionID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(updatedMission,status);
        }
        if(!Objects.equals(missionID,mission.getId())) {
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(updatedMission,status);
        }
        updatedMission = missionRepository.save(mission);
        status = HttpStatus.OK;
        return new ResponseEntity<>(updatedMission,status);
    }

    public ResponseEntity<Mission> deleteMission(Long gameID, Long missionID) {
        HttpStatus status;
        Mission deletedMission = new Mission();
        if(!gameRepository.existsById(gameID) || !missionRepository.existsById(missionID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(deletedMission,status);
        }
        //deleted mission
        deletedMission = missionRepository.findById(missionID).get();
        missionRepository.deleteById(missionID);
        //Updates missions in specified game, tror ikke det er nødvendig
        /*
        Game game = gameRepository.findById(gameID).get();
        Set<Mission> missions = game.getMissions();
        missions.remove(deletedMission);
        game.setMissions(missions);
        gameRepository.save(game);

         */
        //returns new mission with code
        status = HttpStatus.OK;
        return new ResponseEntity<>(deletedMission, status);
    }
}
