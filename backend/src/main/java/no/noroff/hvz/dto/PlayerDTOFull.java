package no.noroff.hvz.dto;

import java.util.List;

public class PlayerDTOFull implements PlayerDTO{

    private Long id;
    private Boolean isHuman;
    private String biteCode;
    private AppUserDTO user;
    private String killsUrl;
    private String messagesUrl;


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

    public AppUserDTO getUser() {
        return user;
    }

    public void setUser(AppUserDTO user) {
        this.user = user;
    }

    public String getKillsUrl() {
        return killsUrl;
    }

    public void setKillsUrl(String killsUrl) {
        this.killsUrl = killsUrl;
    }

    public String getMessagesUrl() {
        return messagesUrl;
    }

    public void setMessagesUrl(String messagesUrl) {
        this.messagesUrl = messagesUrl;
    }
}
