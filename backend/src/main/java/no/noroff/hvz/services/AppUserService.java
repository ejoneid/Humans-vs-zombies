package no.noroff.hvz.services;

import no.noroff.hvz.exceptions.AppUserAlreadyExistException;
import no.noroff.hvz.exceptions.AppUserNotFoundException;
import no.noroff.hvz.exceptions.MissingPlayerException;
import no.noroff.hvz.models.AppUser;
import no.noroff.hvz.models.Game;
import no.noroff.hvz.models.Player;
import no.noroff.hvz.repositories.AppUserRepository;
import no.noroff.hvz.repositories.GameRepository;
import no.noroff.hvz.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

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

    public Player getPlayerByGameAndUser(Long gameId, AppUser user) throws MissingPlayerException {
        Game game = gameRepository.findById(gameId).get();
        if(!playerRepository.existsByGameAndUser(game,user)) throw new MissingPlayerException();
        Player player = playerRepository.getPlayerByGameAndUser(game,user);
        return player;
    }

    public AppUser createUser(AppUser addedUser) throws AppUserAlreadyExistException {
        if (appUserRepository.existsAppUserByOpenId(addedUser.getOpenId())) throw new AppUserAlreadyExistException();
        return appUserRepository.save(addedUser);
    }
}
