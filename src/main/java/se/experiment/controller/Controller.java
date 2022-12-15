package se.experiment.controller;

import se.experiment.exceptions.AdhocDBException;
import se.experiment.integration.AdhocDAO;
import se.experiment.model.AdhocIndividual;
import se.experiment.model.AdhocTestResults;
import se.experiment.model.IndividualDTO;

import java.util.List;

public class Controller {

    private final AdhocDAO adhocDb;

    public Controller() throws AdhocDBException {
        this.adhocDb = new AdhocDAO();
    }

    public List<? extends IndividualDTO> getAllAdhocIndividuals() throws Exception {

        try {
            return adhocDb.getAllIndividuals();
        } catch (Exception e ) {
            throw new Exception("Couldn't get individuals", e);
        }
    }

    public AdhocTestResults runAdhocReadTestOne() {

        return new AdhocTestResults();
    }
}
