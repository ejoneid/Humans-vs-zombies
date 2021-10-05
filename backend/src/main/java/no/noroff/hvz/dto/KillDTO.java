package no.noroff.hvz.dto;

import java.util.Date;

public class KillDTO {

    private Long id;
    private Date timeOfDeath;
    private String story;
    private String lat;
    private String lng;
    private String killerUrl;
    private String victimUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimeOfDeath() {
        return timeOfDeath;
    }

    public void setTimeOfDeath(Date timeOfDeath) {
        this.timeOfDeath = timeOfDeath;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getKillerUrl() {
        return killerUrl;
    }

    public void setKillerUrl(String killerUrl) {
        this.killerUrl = killerUrl;
    }

    public String getVictimUrl() {
        return victimUrl;
    }

    public void setVictimUrl(String victimUrl) {
        this.victimUrl = victimUrl;
    }
}
