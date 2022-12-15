package se.experiment.controller;

import se.experiment.exceptions.AdhocDBException;
import se.experiment.integration.AdhocDAO;
import se.experiment.model.Test;
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

    public void runAdhocReadTestOne(Test test) throws AdhocDBException {
        for (int i = 0; i < test.getAmountOfTests(); i ++)
            adhocDb.runAdhocReadTestOne(test);
    }

    public void runAdhocWriteTestOne(Test test) throws AdhocDBException {
        for (int i = 0; i < test.getAmountOfTests(); i ++)
            adhocDb.runAdhocWriteTestOne(test);
    }
}
