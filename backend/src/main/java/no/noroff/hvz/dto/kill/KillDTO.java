package no.noroff.hvz.dto.kill;

import java.util.Date;

public class KillDTO {

    private Long id;
    private Date timeOfDeath;
    private String story;
    private String lat;
    private String lng;
    private String killerName;
    private String victimName;

    public KillDTO(Long id, Date timeOfDeath, String story, String lat, String lng, String killerUrl, String victimUrl) {
        this.id = id;
        this.timeOfDeath = timeOfDeath;
        this.story = story;
        this.lat = lat;
        this.lng = lng;
        this.killerName = killerUrl;
        this.victimName = victimUrl;
    }

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

    public String getKillerName() {
        return killerName;
    }

    public void setKillerName(String killerName) {
        this.killerName = killerName;
    }

    public String getVictimName() {
        return victimName;
    }

    public void setVictimName(String victimName) {
        this.victimName = victimName;
    }
}