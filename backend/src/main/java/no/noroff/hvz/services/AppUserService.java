package no.noroff.hvz.services;

import no.noroff.hvz.exceptions.AppUserAlreadyExistException;
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

    /**
     * Method for getting a specific user from the DB
     * @param openId
     * @return The requested AppUser
     * @throws AppUserNotFoundException
     */
    public AppUser getSpecificUser(String openId) throws AppUserNotFoundException {
        if(!appUserRepository.existsAppUserByOpenId(openId)) {
            throw new AppUserNotFoundException("Could not find user");
        }
        return appUserRepository.getAppUserByOpenId(openId);
    }

    /**
     * Method for getting a player based on the user and game
     * @param gameId
     * @param user
     * @return The wanted player object
     */
    public Player getPlayerByGameAndUser(Long gameId, AppUser user) {
        Player player = null;
        Game game = gameRepository.getById(gameId);
        if(playerRepository.existsByGameAndUser(game,user)) {
            player = playerRepository.getPlayerByGameAndUser(game,user);
        }
        return player;
    }

    /**
     * Method for adding a user to the DB
     * @param addedUser
     * @return Saved object/error
     * @throws AppUserAlreadyExistException
     */
    public AppUser createUser(AppUser addedUser) throws AppUserAlreadyExistException {
        if (appUserRepository.existsAppUserByOpenId(addedUser.getOpenId())) throw new AppUserAlreadyExistException();
        return appUserRepository.save(addedUser);
    }
}
