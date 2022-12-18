package se.experiment.controller;

import se.experiment.exceptions.AdhocDBException;
import se.experiment.exceptions.NormDBException;
import se.experiment.integration.AdhocDAO;
import se.experiment.integration.NormDAO;
import se.experiment.model.Test;
import se.experiment.model.IndividualDTO;

import java.util.List;

public class Controller {

    private final AdhocDAO adhocDb;
    private final NormDAO normDb;

    public Controller() throws AdhocDBException, NormDBException {
        this.adhocDb = new AdhocDAO();
        this.normDb = new NormDAO();
    }

    public List<? extends IndividualDTO> getAllAdhocIndividuals() throws Exception {
        try {
            return adhocDb.getAllIndividuals();
        } catch (Exception e) {
            throw new Exception("Couldn't get individuals", e);
        }
    }

    public void runAdhocReadTestOne(Test test) throws AdhocDBException {
        adhocDb.runAdhocReadTestOne(test);
    }

    public void runAdhocReadTestTwo(Test test) throws AdhocDBException {
        adhocDb.runAdhocReadTestTwo(test);
    }

    public void runAdhocReadTestThree(Test test) throws AdhocDBException {
        adhocDb.runAdhocReadTestThree(test);
    }

    public void runAdhocReadTestFour(Test test) throws AdhocDBException {
        adhocDb.runAdhocReadTestFour(test);
    }

    public void runAdhocWriteTestOne(Test test) throws AdhocDBException {
        adhocDb.runAdhocWriteTestOne(test);
    }

    public void runAdhocUpdateTestOne(Test test) throws AdhocDBException {
        adhocDb.runAdhocUpdateTestOne(test);
    }
    public void runAdhocUpdateTestTwo(Test test) throws AdhocDBException {
        adhocDb.runAdhocUpdateTestTwo(test);
    }

    public void runNormReadTestOne(Test test) throws NormDBException {
        normDb.runNormReadTestOne(test);
    }

    public void runNormReadTestTwo(Test test) throws NormDBException {
        normDb.runNormReadTestTwo(test);
    }

    public void runNormReadTestThree(Test test) throws NormDBException {
        normDb.runNormReadTestThree(test);
    }

    public void runNormReadTestFour(Test test) throws NormDBException {
        normDb.runNormReadTestFour(test);
    }

    public void runNormWriteTestOne(Test test) throws NormDBException {
        normDb.runNormWriteTestOne(test);
    }

    public void runNormUpdateTestOne(Test test) throws NormDBException {
        normDb.runNormUpdateTestOne(test);
    }

    public void runNormUpdateTestTwo(Test test) throws NormDBException {
        normDb.runNormUpdateTestTwo(test);
    }

}
