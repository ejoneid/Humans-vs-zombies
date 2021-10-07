package no.noroff.hvz.dto;

public class RegKillDTO {

    private Long killerID;
    private String byteCode;
    private String story;
    private String lat;
    private String lng;

    public Long getKillerID() {
        return killerID;
    }

    public void setKillerID(Long killerID) {
        this.killerID = killerID;
    }

    public String getByteCode() {
        return byteCode;
    }

    public void setByteCode(String byteCode) {
        this.byteCode = byteCode;
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
