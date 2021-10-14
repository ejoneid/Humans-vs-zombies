package no.noroff.hvz.services;

import no.noroff.hvz.exceptions.AppUserNotFoundException;
import no.noroff.hvz.models.AppUser;
import no.noroff.hvz.models.Game;
import no.noroff.hvz.models.Player;
import no.noroff.hvz.repositories.AppUserRepository;
import no.noroff.hvz.repositories.GameRepository;
import no.noroff.hvz.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;

    public AppUser getSpecificUser(String openId) throws AppUserNotFoundException {
        if(!appUserRepository.existsAppUserByOpenId(openId)) {
            throw new AppUserNotFoundException("Could not find user");
        }
        return appUserRepository.getAppUserByOpenId(openId);
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
