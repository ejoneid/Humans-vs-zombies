package no.noroff.hvz;

import no.noroff.hvz.models.*;
import no.noroff.hvz.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
class AppStartupRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(AppStartupRunner.class);

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    KillerRepository killerRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MissionRepository missionRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    SquadCheckInRepository squadCheckInRepository;

    @Autowired
    SquadMemberRepository squadMemberRepository;

    @Autowired
    SquadRepository squadRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Your application started with option names : {}", args.getOptionNames());

        AppUser user1 = new AppUser();
        AppUser user2 = new AppUser();
        AppUser user3 = new AppUser();

        Game game1 = new Game();

        Kill kill1 = new Kill();
        Kill kill2 = new Kill();
        Kill kill3 = new Kill();

        Message message1 = new Message();
        Message message2 = new Message();
        Message message3 = new Message();

        Mission mission1 = new Mission();

        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();

        Squad squad1 = new Squad();

        SquadCheckIn sci1 = new SquadCheckIn();

        SquadMember member1 = new SquadMember();

        game1.setGameState("Some state");
        game1.setName("The coolest game");
        if (gameRepository.count() == 0) {
            gameRepository.save(game1);
        }


        mission1.setGame(game1);
        mission1.setHuman(true);
        mission1.setName("The coolest mission");

        if (missionRepository.count() == 0) {
            mission1 = missionRepository.save(mission1);
        }

        user1.setFirstName("A");
        user1.setLastName("A");

        user2.setFirstName("B");
        user2.setLastName("B");

        user3.setFirstName("C");
        user3.setLastName("C");

        if (appUserRepository.count() == 0) {
            user1 = appUserRepository.save(user1);
            user2 = appUserRepository.save(user2);
            user3 = appUserRepository.save(user3);
        }

        player1.setGame(game1);
        player1.setHuman(true);
        player1.setBiteCode("placeholder 1");
        player1.setUser(user1);
        player1.setPatientZero(true);

        player2.setGame(game1);
        player2.setHuman(true);
        player2.setBiteCode("placeholder 2");
        player2.setUser(user2);
        player2.setPatientZero(false);

        player3.setGame(game1);
        player3.setHuman(true);
        player3.setBiteCode("placeholder 3");
        player3.setUser(user3);
        player3.setPatientZero(false);

        if (playerRepository.count() == 0) {
            player1 = playerRepository.save(player1);
            player2 = playerRepository.save(player2);
            player3 = playerRepository.save(player3);
        }

        kill1.setGame(game1);
        kill1.setKiller(player1);
        kill1.setVictim(player2);
        kill1.setTimeOfDeath(new Date());

        if (killerRepository.count() == 0) {
            kill1 = killerRepository.save(kill1);
        }

        squad1.setGame(game1);
        squad1.setHuman(true);
        squad1.setName("The cool gang");
        if (squadRepository.count() == 0) {
            squadRepository.save(squad1);
        }

        message1.setMessage("A very cool message");
        message1.setHuman(true);
        message1.setZombie(true);
        message1.setPlayer(player1);
        message1.setGame(game1);
        message1.setSquad(squad1);

        if (messageRepository.count() == 0) {
            message1 = messageRepository.save(message1);
        }

        member1.setSquad(squad1);
        member1.setRank("A high rank, like officer");
        member1.setPlayer(player1);
        if (squadMemberRepository.count() == 0) {
            squadMemberRepository.save(member1);
        }

        sci1.setMember(member1);
        sci1.setStartTime(new Date());
        sci1.setEndTime(new Date());
        sci1.setLat("Some lat");
        sci1.setLng("Some long");
        if (squadCheckInRepository.count() == 0) {
            squadCheckInRepository.save(sci1);
        }
    }
}