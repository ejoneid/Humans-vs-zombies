package no.noroff.hvz.dto;

import java.util.List;

public class PlayerDTOStandard implements PlayerDTO{

    private Long id;
    private Boolean isHuman;
    private String killsUrl;


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
}
