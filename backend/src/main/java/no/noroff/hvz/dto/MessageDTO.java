package no.noroff.hvz.dto;

import java.util.Date;

public class MessageDTO {
    private Long id;
    private String message;
    private Date messageTime;
    private String playerUrl;

    public MessageDTO(Long id, String message, Date messageTime, String playerUrl) {
        this.id = id;
        this.message = message;
        this.messageTime = messageTime;
        this.playerUrl = playerUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
    }

    public String getPlayerUrl() {
        return playerUrl;
    }

    public void setPlayerUrl(String playerUrl) {
        this.playerUrl = playerUrl;
    }
}
