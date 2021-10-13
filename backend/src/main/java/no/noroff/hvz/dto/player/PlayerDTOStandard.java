package no.noroff.hvz.dto.player;

import no.noroff.hvz.dto.player.PlayerDTO;
import no.noroff.hvz.dto.user.AppUserDTO;

public class PlayerDTOStandard implements PlayerDTO {

    private Long id;
    private Boolean isHuman;
    private String killsUrl;
    private AppUserDTO user;

    public PlayerDTOStandard(Long id, Boolean isHuman, String killsUrl, AppUserDTO user) {
        this.id = id;
        this.isHuman = isHuman;
        this.killsUrl = killsUrl;
        this.user = user;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getHuman() {
        return isHuman;
    }

    public void setHuman(Boolean human) {
        isHuman = human;
    }

    public String getKillsUrl() {
        return killsUrl;
    }

    public void setKillsUrl(String killsUrl) {
        this.killsUrl = killsUrl;
    }

    public AppUserDTO getUser() {
        return user;
    }

    public void setUser(AppUserDTO user) {
        this.user = user;
    }
}
