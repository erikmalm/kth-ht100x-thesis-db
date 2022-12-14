package se.experiment.model;

public class AdhocIndividual implements IndividualDTO {
    private String id;
    private String personalId;

    public AdhocIndividual(String id, String personalId) {
        this.id = id;
        this.personalId = personalId;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getPersonalId() {
        return personalId;
    }

    public String toString() {
        return id + " | " + personalId;
    }


}
