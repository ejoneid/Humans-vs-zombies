package no.noroff.hvz.dto.player;

public class PlayerDTORegAdmin {

    private Long userID;
    private boolean human;
    private boolean patientZero;

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public boolean isHuman() {
        return human;
    }

    public void setHuman(boolean human) {
        this.human = human;
    }

    public boolean isPatientZero() {
        return patientZero;
    }

    public void setPatientZero(boolean patientZero) {
        this.patientZero = patientZero;
    }
}
