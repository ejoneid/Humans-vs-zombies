package no.noroff.hvz.mapper;

import no.noroff.hvz.dto.game.GameDTO;
import no.noroff.hvz.dto.kill.KillDTO;
import no.noroff.hvz.dto.kill.KillDTOReg;
import no.noroff.hvz.dto.message.MessageDTO;
import no.noroff.hvz.dto.mission.MissionDTO;
import no.noroff.hvz.dto.player.*;
import no.noroff.hvz.dto.squad.*;
import no.noroff.hvz.dto.user.AppUserDTO;
import no.noroff.hvz.exceptions.InvalidBiteCodeException;
import no.noroff.hvz.models.*;
import no.noroff.hvz.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class Mapper {

    private final String url = "/api/game/";

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SquadRepository squadRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private SquadMemberRepository squadMemberRepository;

    @Autowired
    private CustomMapper customMapper;

    public MissionDTO toMissionDTO(Mission mission) {
        return new MissionDTO(mission.getId(),mission.getName(),mission.getDescription(),
                mission.getStartTime(), mission.getEndTime(), mission.isHuman(), mission.getLat(), mission.getLng(),mission.getGame().getId());
    }

    public Mission toMission(MissionDTO missionDTO) {
        Game game = gameRepository.getById(missionDTO.getGameId());
        return new Mission(missionDTO.getId(), missionDTO.getName(), missionDTO.isHuman(), missionDTO.getDescription(),
                missionDTO.getStartTime(), missionDTO.getEndTime(), missionDTO.getLat(), missionDTO.getLng(), game);
    }

    public AppUserDTO toAppUserDTO(AppUser user) {
        return new AppUserDTO(user.getFirstName(), user.getLastName());
    }

    public AppUser toAppUser(AppUserDTO appUserDTO) {
        AppUser appUser = new AppUser();
        appUser.setLastName(appUserDTO.getLastName());
        appUser.setFirstName(appUserDTO.getFirstName());
        return appUser;
    }

    public GameDTO toGameDTO(Game game) {
        String gameUrl = url + game.getId();
        String squadsUrl = gameUrl + "/squad";
        String missionsUrl = gameUrl + "/mission";
        String killsUrl = gameUrl + "/kill";
        String chatUrl = gameUrl + "/chat";
        String playersUrl = gameUrl + "/player";
        return new GameDTO(game.getId(), game.getName(),game.getGameState(), game.getDescription(), game.getNw_lat(), game.getSe_lat(),
                game.getNw_long(),game.getSe_long(),squadsUrl, missionsUrl, killsUrl, chatUrl, playersUrl);
    }

    public KillDTO toKillDTO(Kill kill) {
        String killerName = kill.getKiller().getUser().getFirstName() + " " + kill.getKiller().getUser().getLastName();
        String victimName = kill.getVictim().getUser().getFirstName() + " " + kill.getVictim().getUser().getLastName();
        return new KillDTO(kill.getId(), kill.getTimeOfDeath(), kill.getStory(), kill.getLat(), kill.getLng(),
                killerName, victimName);
    }

    public Kill regKillDTO(KillDTOReg killDTO) throws InvalidBiteCodeException {
        Kill kill = new Kill();
        customMapper.updateKillFromDto(killDTO, kill);
        Player killer = playerRepository.findById(killDTO.getKillerID()).get();
        Player victim = playerRepository.getPlayerByGameAndBiteCode(killer.getGame(), killDTO.getBiteCode());
        if (victim == null) throw new InvalidBiteCodeException("BiteCode did not match any players in this game!");
        kill.setKiller(killer);
        kill.setVictim(victim);
        return kill;
    }

    public MessageDTO toMessageDTO(Message message) {
        String playerUrl = url + message.getGame().getId() + "/player/" + message.getPlayer().getId();
        return new MessageDTO(message.getId(), message.getMessage(),message.getChatTime(),playerUrl, message.getPlayer().getUser().getFirstName() + " " + message.getPlayer().getUser().getLastName(), message.isHuman(), message.isGlobal(), message.isFaction());
    }

    public PlayerDTOStandard toPlayerDTOStandard(Player player) {
        return new PlayerDTOStandard(player.getId(),player.isHuman(), player.getBiteCode());
    }

    public Player toPlayer(PlayerDTO playerDTO) {
        return playerRepository.getById(playerDTO.getId());
    }

    public PlayerDTOFull toPlayerDTOFull(Player player) {
        String killsUrl = url + player.getGame().getId() + "/kill/"; //TODO legge til searc parameter så vi får riktige kills
        String messagesUrl = url + player.getGame().getId() + "/chat/"; //TODO legge til searc parameter så vi får riktige messages
        AppUserDTO userDTO = toAppUserDTO(player.getUser());
        return new PlayerDTOFull(player.getId(),player.isHuman(), player.getBiteCode(),
               userDTO ,killsUrl,messagesUrl);
    }

    public Player regPlayerDTO(PlayerDTOReg p) {
        Player player = new Player();
        if (!appUserRepository.existsById(p.getUserID())) return null;
        AppUser user = appUserRepository.findById(p.getUserID()).get();
        player.setUser(user);
        player.setHuman(true);
        player.setPatientZero(false);
        return player;
    }

    public SquadMember toSquadMember (SquadMemberFromDTO dto) {
        SquadMember member = new SquadMember();
        Player player = playerRepository.findById(dto.getPlayerID()).get();
        member.setPlayer(player);
        member.setRank(dto.getRank());
        return member;
    }

    public SquadDTO toSquadDTO(Squad squad) {
        ArrayList<PlayerDTO> members = new ArrayList<>();
        for (SquadMember member: squad.getMembers()) {
            members.add(toPlayerDTOStandard(member.getPlayer()));
        }
        return new SquadDTO(squad.getId(), squad.getName(),members);
    }

    public SquadCheckInDTO toSquadCheckInDTO(SquadCheckIn squadCheckIn) {
        PlayerDTO memberDTO = toPlayerDTOStandard(squadCheckIn.getMember().getPlayer());
        return new SquadCheckInDTO(squadCheckIn.getId(), squadCheckIn.getTime(),
                squadCheckIn.getLat(), squadCheckIn.getLng(), memberDTO);
    }

    public SquadCheckIn toSquadCheckIn(SquadCheckInDTO squadCheckInDTO) {
        Player player = toPlayer(squadCheckInDTO.getMember());
        SquadMember squadMember = squadMemberRepository.getByPlayer(player);
        return new SquadCheckIn(squadCheckInDTO.getId(), squadCheckInDTO.getTime(), squadCheckInDTO.getLat(), squadCheckInDTO.getLng(), squadMember);
    }
}
