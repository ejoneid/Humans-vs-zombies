package no.noroff.hvz.dto.player;

import no.noroff.hvz.dto.player.PlayerDTO;
import no.noroff.hvz.dto.user.AppUserDTO;

public class PlayerDTOFull implements PlayerDTO {

    private Long id;
    private Boolean isHuman;
    private Boolean patientZero;
    private String biteCode;
    private AppUserDTO user;
    private String killsUrl;
    private String messagesUrl;

    public PlayerDTOFull(Long id, Boolean isHuman, Boolean patientZero, String biteCode, AppUserDTO user, String killsUrl, String messagesUrl) {
        this.id = id;
        this.isHuman = isHuman;
        this.patientZero = patientZero;
        this.biteCode = biteCode;
        this.user = user;
        this.killsUrl = killsUrl;
        this.messagesUrl = messagesUrl;
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

    public Boolean getPatientZero() {
        return patientZero;
    }

    public void setPatientZero(Boolean patientZero) {
        this.patientZero = patientZero;
    }
}
