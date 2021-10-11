package no.noroff.hvz.services;

import no.noroff.hvz.dto.AppUserDTO;
import no.noroff.hvz.models.AppUser;
import no.noroff.hvz.repositories.AppUserRepository;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class UserService {

    @Autowired
    private AppUserRepository appUserRepository;

    public AppUser getSpecificUser(Long id) {
        AppUser appUser = null;
        if(appUserRepository.existsById(id)) {
            appUser = appUserRepository.getById(id);
        }
        return appUser;
    }

    public AppUser createUser(AppUser addedUser) {
        return appUserRepository.save(addedUser);
    }
}
