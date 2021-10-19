package no.noroff.hvz.dto.player;

import no.noroff.hvz.dto.user.AppUserDTOFull;
import no.noroff.hvz.dto.user.AppUserDTOReg;

public class PlayerDTOFull implements PlayerDTO {

    private Long id;
    private Boolean isHuman;
    private Boolean patientZero;
    private String biteCode;
    private Long gameID;
    private AppUserDTOFull user;
    private String killsUrl;
    private String messagesUrl;

    public PlayerDTOFull(Long id, Boolean isHuman, Boolean patientZero, String biteCode, Long gameID, AppUserDTOFull user, String killsUrl, String messagesUrl) {
        this.id = id;
        this.isHuman = isHuman;
        this.patientZero = patientZero;
        this.biteCode = biteCode;
        this.gameID = gameID;
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

    public AppUserDTOFull getUser() {
        return user;
    }

    public void setUser(AppUserDTOFull user) {
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

    public Long getGameID() {
        return gameID;
    }

    public void setGameID(Long gameID) {
        this.gameID = gameID;
    }

}
