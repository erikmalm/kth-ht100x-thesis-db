package se.experiment.controller;

import se.experiment.exceptions.AdhocDBException;
import se.experiment.exceptions.NormDBException;
import se.experiment.integration.AdhocDAO;
import se.experiment.integration.NormDAO;
import se.experiment.model.Test;
import se.experiment.model.IndividualDTO;

import java.util.List;

/**
 * This class acts as a controller for the application, coordinating interactions
 * between the model and the view.
 */
public class Controller {

    private final AdhocDAO adhocDb;
    private final NormDAO normDb;

    /**
     * Creates a new instance of the controller.
     *
     * @throws AdhocDBException if there is an error connecting to the Adhoc database
     * @throws NormDBException if there is an error connecting to the Norm database
     */
    public Controller() throws AdhocDBException, NormDBException {
        this.adhocDb = new AdhocDAO();
        this.normDb = new NormDAO();
    }

    /**
     * Returns a list of all individuals in the Adhoc database.
     *
     * @return a list of all individuals in the Adhoc database
     * @throws Exception if there is an error retrieving the individuals from the database
     */
    public List<? extends IndividualDTO> getAllAdhocIndividuals() throws Exception {
        try {
            return adhocDb.getAllIndividuals();
        } catch (Exception e) {
            throw new Exception("Couldn't get individuals", e);
        }
    }

    /**
     * Runs test one for reading from the Adhoc database.
     *
     * @param test the test to run
     * @throws AdhocDBException if there is an error running the test
     */
    public void runAdhocReadTestOne(Test test) throws AdhocDBException {
        adhocDb.runAdhocReadTestOne(test);
    }

    /**
     * Runs test two for reading from the Adhoc database.
     *
     * @param test the test to run
     * @throws AdhocDBException if there is an error running the test
     */
    public void runAdhocReadTestTwo(Test test) throws AdhocDBException {
        adhocDb.runAdhocReadTestTwo(test);
    }

    /**
     * Runs test three for reading from the Adhoc database.
     *
     * @param test the test to run
     * @throws AdhocDBException if there is an error running the test
     */
    public void runAdhocReadTestThree(Test test) throws AdhocDBException {
        adhocDb.runAdhocReadTestThree(test);
    }

    /**
     * Runs test four for reading from the Adhoc database.
     *
     * @param test the test to run
     * @throws AdhocDBException if there is an error running the test
     */
    public void runAdhocReadTestFour(Test test) throws AdhocDBException {
        adhocDb.runAdhocReadTestFour(test);
    }

    public void runAdhocWriteTestOne(Test test) throws AdhocDBException {
        adhocDb.runAdhocWriteTestOne(test);
    }
    public void runAdhocWriteTestTwo(Test test) throws AdhocDBException {
        adhocDb.runAdhocWriteTestTwo(test);
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

    public void runNormWriteTestTwo(Test test) throws NormDBException {
        normDb.runNormWriteTestTwo(test);
    }

    public void runNormUpdateTestOne(Test test) throws NormDBException {
        normDb.runNormUpdateTestOne(test);
    }

    public void runNormUpdateTestTwo(Test test) throws NormDBException {
        normDb.runNormUpdateTestTwo(test);
    }

    public void runNormDeleteTestOne(Test test) throws NormDBException {
        normDb.runNormDeleteTestOne(test);
    }

    public void runNormDeleteTestTwo(Test test) throws NormDBException {
        normDb.runNormDeleteTestTwo(test);
    }
}
