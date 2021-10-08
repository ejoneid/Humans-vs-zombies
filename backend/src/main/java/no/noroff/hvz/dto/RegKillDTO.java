package no.noroff.hvz.dto;

public class RegKillDTO {

    private Long killerID;
    private String biteCode;
    private String story;
    private String lat;
    private String lng;

    public Long getKillerID() {
        return killerID;
    }

    public void setKillerID(Long killerID) {
        this.killerID = killerID;
    }

    public String getBiteCode() {
        return biteCode;
    }

    public void setByteCode(String biteCode) {
        this.biteCode = biteCode;
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
}
