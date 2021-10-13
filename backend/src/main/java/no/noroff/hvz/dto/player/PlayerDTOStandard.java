package no.noroff.hvz.dto.player;

import no.noroff.hvz.dto.player.PlayerDTO;
import no.noroff.hvz.dto.user.AppUserDTO;

public class PlayerDTOStandard implements PlayerDTO {

    private Long id;
    private Boolean isHuman;
    private String biteCode;

    public PlayerDTOStandard(Long id, Boolean isHuman, String biteCode) {
        this.id = id;
        this.isHuman = isHuman;
        this.biteCode = biteCode;
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

    public String getBiteCode() {
        return biteCode;
    }

    public void setBiteCode(String biteCode) {
        this.biteCode = biteCode;
    }
}
