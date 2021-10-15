package no.noroff.hvz.dto.player;

public class PlayerDTOUpdate {

    private boolean human;
    private boolean patientZero;

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
