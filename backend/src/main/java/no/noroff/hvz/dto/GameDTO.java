package no.noroff.hvz.dto;

import javax.persistence.Column;

public class GameDTO {

    private Long id;
    private String name;
    private String gameState;
    private String nw_lat;
    private String se_lat;
    private String nw_long;
    private String se_long;
    private String squadsUrl;
    private String missionsUrl;
    private String killsUrl;
    private String messagesUrl;
    private String playersUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public String getNw_lat() {
        return nw_lat;
    }

    public void setNw_lat(String nw_lat) {
        this.nw_lat = nw_lat;
    }

    public String getSe_lat() {
        return se_lat;
    }

    public void setSe_lat(String se_lat) {
        this.se_lat = se_lat;
    }

    public String getNw_long() {
        return nw_long;
    }

    public void setNw_long(String nw_long) {
        this.nw_long = nw_long;
    }

    public String getSe_long() {
        return se_long;
    }

    public void setSe_long(String se_long) {
        this.se_long = se_long;
    }

    public String getSquadsUrl() {
        return squadsUrl;
    }

    public void setSquadsUrl(String squadsUrl) {
        this.squadsUrl = squadsUrl;
    }

    public String getMissionsUrl() {
        return missionsUrl;
    }

    public void setMissionsUrl(String missionsUrl) {
        this.missionsUrl = missionsUrl;
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

    public String getPlayersUrl() {
        return playersUrl;
    }

    public void setPlayersUrl(String playersUrl) {
        this.playersUrl = playersUrl;
    }
}
