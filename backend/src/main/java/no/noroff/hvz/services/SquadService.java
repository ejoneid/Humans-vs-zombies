package no.noroff.hvz.services;

import no.noroff.hvz.models.*;
import no.noroff.hvz.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Service
public class SquadService {

    @Autowired
    SquadRepository squadRepository;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    SquadCheckInRepository squadCheckInRepository;

    @Autowired
    SquadMemberRepository squadMemberRepository;

    private HttpStatus status;

    public ResponseEntity<List<Squad>> getAllSquads(Long gameID) {
        List<Squad> squads = new ArrayList<>();
        if(!gameRepository.existsById(gameID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(squads,status);
        }
        Game game = gameRepository.findById(gameID).get();
        squads = new ArrayList<>(game.getSquads());
        status = HttpStatus.OK;
        return new ResponseEntity<>(squads,status);
    }

    public ResponseEntity<Squad> getSpecificSquad(Long gameID, Long squadID) {
        Squad squad = new Squad();
        if (!squadRepository.existsById(squadID) || !gameRepository.existsById(gameID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(squad, status);
        }
        squad = squadRepository.findById(squadID).get();
        status = HttpStatus.OK;
        return new ResponseEntity<>(squad, status);
    }

    public ResponseEntity<Squad> createNewSquad(Long gameID, Squad squad) {
        Squad createdSquad = new Squad();
        if(!gameRepository.existsById(gameID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(createdSquad,status);
        }
        createdSquad = squadRepository.save(squad);
        status = HttpStatus.CREATED;
        return new ResponseEntity<>(createdSquad, status);
    }

    public ResponseEntity<SquadMember> joinSquad(Long gameID, Long squadID, SquadMember member) {
        if(!squadRepository.existsById(squadID) || !gameRepository.existsById(gameID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(member,status);
        }
        member.setSquad(squadRepository.getById(squadID));
        squadMemberRepository.save(member);
        status = HttpStatus.CREATED;
        return new ResponseEntity<>(member, status);
    }

    public ResponseEntity<Squad> updateSquad(Long gameID, Long squadID, Squad squad) {
        Squad updatedSquad = new Squad();
        if(!gameRepository.existsById(gameID) || !squadRepository.existsById(squadID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(updatedSquad,status);
        }
        if(!Objects.equals(squadID, squad.getId())) {
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(updatedSquad,status);
        }
        updatedSquad = squadRepository.save(squad);
        status = HttpStatus.OK;
        return new ResponseEntity<>(updatedSquad,status);
    }

    public ResponseEntity<Squad> deleteSquad(Long gameID, Long squadID) {
        Squad deletedSquad = new Squad();
        if(!gameRepository.existsById(gameID) || !squadRepository.existsById(squadID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(deletedSquad,status);
        }
        deletedSquad = squadRepository.findById(squadID).get();
        squadRepository.deleteById(squadID);
        status = HttpStatus.OK;
        return new ResponseEntity<>(deletedSquad, status);
    }

    public ResponseEntity<List<Message>> getSquadChat(Long gameID, Long squadID) {
        List<Message> chat = new ArrayList<>();
        if(!gameRepository.existsById(gameID) || !squadRepository.existsById(squadID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(chat,status);
        }
        Squad squad = squadRepository.findById(squadID).get();
        chat = squad.getMessages().stream().toList();
        status = HttpStatus.OK;
        return new ResponseEntity<>(chat, status);
    }

    public ResponseEntity<Message> createSquadChat(Long gameID, Long squadID, Message message) {
        if(!gameRepository.existsById(gameID) || !squadRepository.existsById(squadID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(message,status);
        }
        messageRepository.save(message);
        status = HttpStatus.CREATED;
        return new ResponseEntity<>(message, status);
    }

    public ResponseEntity<List<SquadCheckIn>> getSquadCheckIn(Long gameID, Long squadID) {
        List<SquadCheckIn> checkins = new ArrayList<>();
        if(!gameRepository.existsById(gameID) || !squadRepository.existsById(squadID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(checkins,status);
        }
        Squad squad = squadRepository.findById(squadID).get();
        Set<SquadMember> members = squad.getMembers();
        for (SquadMember m : members) {
            checkins.addAll(m.getCheckIns());
        }
        status = HttpStatus.OK;
        return new ResponseEntity<>(checkins, status);
    }

    public ResponseEntity<SquadCheckIn> createSquadCheckIn(Long gameID, Long squadID, SquadCheckIn checkin) {
        if(!gameRepository.existsById(gameID) || !squadRepository.existsById(squadID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(checkin,status);
        }
        squadCheckInRepository.save(checkin);
        status = HttpStatus.CREATED;
        return new ResponseEntity<>(checkin, status);
    }
}
