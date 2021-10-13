package no.noroff.hvz.services;

import no.noroff.hvz.dto.AppUserDTO;
import no.noroff.hvz.models.AppUser;
import no.noroff.hvz.models.Game;
import no.noroff.hvz.models.Player;
import no.noroff.hvz.repositories.AppUserRepository;
import no.noroff.hvz.repositories.GameRepository;
import no.noroff.hvz.repositories.PlayerRepository;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class UserService {

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;

    public AppUser getSpecificUser(String openId) {
        AppUser appUser = null;
        if(appUserRepository.existsAppUserByOpenId(openId)) {
            appUser = appUserRepository.getAppUserByOpenId(openId);
        }
        return appUser;
    }

    public Player getPlayerByGameAndUser(Long gameId, AppUser user) {
        Player player = null;
        Game game = gameRepository.getById(gameId);
        if(playerRepository.existsByGameAndUser(game,user)) {
            player = playerRepository.getPlayerByGameAndUser(game,user);
        }
        return player;
    }

    public AppUser createUser(AppUser addedUser) {
        return appUserRepository.save(addedUser);
    }
}
