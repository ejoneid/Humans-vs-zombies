package no.noroff.hvz.dto;

import javax.persistence.Column;
import java.util.Date;

public class SquadCheckInDTO {

    private Long id;
    private Date startTime;
    private Date endTime;
    private String lat;
    private String lng;
    private SquadMemberDTO member;

    public SquadCheckInDTO(Long id, Date startTime, Date endTime, String lat, String lng, SquadMemberDTO member) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lat = lat;
        this.lng = lng;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public SquadMemberDTO getMember() {
        return member;
    }

    public void setMember(SquadMemberDTO member) {
        this.member = member;
    }
}
