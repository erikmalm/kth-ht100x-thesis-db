package se.experiment.integration;

import se.experiment.exceptions.AdhocDBException;
import se.experiment.model.AdhocIndividual;
import se.experiment.model.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdhocDAO {

    // DB Setup
    private final String DB_USERNAME = "postgres";
    private final String DB_PASSWORD = "adnin";
    private final String DB_PATH = "test_adhoc";
    private Connection connection;

    private static final String UNIQUE_PERSONAL_NUMBER = "20000101-1234";

    // TABLES
    private static final String INDIVIDUALS_TABLE_NAME = "public.hallbarheter_privat_id";


    // COLUMNS
    private static final String ID_COL_NAME = "id";
    private static final String PERSONAL_NUMBER_COL_NAME = "person_id";
    private static final String FUND_CAPITAL_COL_NAME = "fondkapital";
    private static final String TRAD_CAPITAL_COL_NAME = "tradkapital";


    //STATEMENTS
    private PreparedStatement getAllAdhocIndividuals;
    private PreparedStatement adHocReadTestOne;
    private PreparedStatement adHocReadTestTwo;
    private PreparedStatement adHocReadTestThree;
    private PreparedStatement adHocWriteTestOne;
    private PreparedStatement resetAdhocWriteTestOne;


    private PreparedStatement prepareAdhocUpdateTestOne;
    private PreparedStatement adhocUpdateTestOne;
    private PreparedStatement resetAdhocUpdateTestOne;


    private void prepareStatements() throws SQLException {

        getAllAdhocIndividuals = connection.prepareStatement(
                "SELECT * " +
                        "FROM " + INDIVIDUALS_TABLE_NAME
        );

        adHocReadTestOne = connection.prepareStatement(
                "SELECT * " +
                        "FROM " + INDIVIDUALS_TABLE_NAME + " " +
                        "ORDER BY " + FUND_CAPITAL_COL_NAME + " ASC " +
                        "LIMIT 1"
        );

        adHocWriteTestOne = connection.prepareStatement(
                "INSERT INTO " + INDIVIDUALS_TABLE_NAME + " " +
                        "(" + PERSONAL_NUMBER_COL_NAME + ") " +
                        "VALUES " + "('"+ UNIQUE_PERSONAL_NUMBER + "')"
        );

        resetAdhocWriteTestOne = connection.prepareStatement(
                "DELETE FROM " + INDIVIDUALS_TABLE_NAME + " " +
                        "WHERE " + PERSONAL_NUMBER_COL_NAME + " = '" + UNIQUE_PERSONAL_NUMBER + "'"
        );

        adhocUpdateTestOne = connection.prepareStatement(
                "UPDATE " + INDIVIDUALS_TABLE_NAME + " " +
                        "SET " + FUND_CAPITAL_COL_NAME + " = 0 " +
                        "WHERE " + PERSONAL_NUMBER_COL_NAME + " = '" + UNIQUE_PERSONAL_NUMBER + "'"
        );

        // These are used again in order to run the update test, but named for clarity
        prepareAdhocUpdateTestOne = adHocWriteTestOne;
        resetAdhocUpdateTestOne = resetAdhocWriteTestOne;

        adHocReadTestTwo = connection.prepareStatement(
                "SELECT COUNT(1) " +
                        "FROM " + INDIVIDUALS_TABLE_NAME + " " +
                        "WHERE " + FUND_CAPITAL_COL_NAME + " > 50000"
        );
        adHocReadTestThree = connection.prepareStatement(
                "SELECT COUNT(*) " +
                        "FROM " + INDIVIDUALS_TABLE_NAME + " " +
                        "WHERE " + TRAD_CAPITAL_COL_NAME + " > 45000"
        );

    }

    public AdhocDAO() throws AdhocDBException {
        System.out.println("Setting up AdhocDAO");
        try {
            connectToAdhocDB();
            prepareStatements();
        } catch (SQLException e) {
            throw new AdhocDBException("Could not connect to database", e);
        }
    }

    private void connectToAdhocDB() throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + DB_PATH,
                "postgres", "admin");
        connection.setAutoCommit(false);
    }

    public List<AdhocIndividual> getAllIndividuals() throws AdhocDBException {

        String failureMessage = "Unable to get all individuals";
        List<AdhocIndividual> individuals = new ArrayList<>();
        ResultSet result = null;



        try {
            //long start = System.nanoTime();
            result = getAllAdhocIndividuals.executeQuery();
            individuals = new ArrayList<AdhocIndividual>();

            while (result.next()) {
                individuals.add(new AdhocIndividual(
                        result.getString(ID_COL_NAME),
                        result.getString(PERSONAL_NUMBER_COL_NAME))
                );
            }

            //long end = System.nanoTime();
            //test.addExecutionTime(end - start);

        } catch (SQLException e) {
            handleException(failureMessage, e);
        } finally {
            closeResultSet(failureMessage, result);
        }

        return individuals;
    }

    private void handleException(String failureMsg, Exception cause) throws AdhocDBException {
        String completeFailureMsg = failureMsg;
        try {
            connection.rollback();
        } catch (SQLException rollbackExc) {
            completeFailureMsg = completeFailureMsg +
                    ". Also failed to rollback query because of: " + rollbackExc.getMessage();
        }

        if (cause != null) {
            throw new AdhocDBException(failureMsg, cause);
        } else {
            throw new AdhocDBException(failureMsg);
        }
    }


    private void closeResultSet(String failureMessage, ResultSet result) throws AdhocDBException {
        try {
            result.close();
        } catch (Exception e) {
            throw new AdhocDBException(failureMessage + " Could not close result set.", e);
        }
    }


    public void runAdhocReadTestOne(Test test) throws AdhocDBException {

        List<AdhocIndividual> individuals = new ArrayList<>();
        ResultSet result = null;

        try {
            long start = System.nanoTime();
            result = adHocReadTestOne.executeQuery();
            individuals = new ArrayList<AdhocIndividual>();

            while (result.next()) {
                individuals.add(new AdhocIndividual(
                        result.getString(ID_COL_NAME),
                        result.getString(PERSONAL_NUMBER_COL_NAME))
                );
            }

            long end = System.nanoTime();
            test.addExecutionTime(end - start);

        } catch (SQLException e) {
            handleException(getFailureMessage(test), e);
        } finally {
            closeResultSet(getFailureMessage(test), result);
        }
    }

    public void runAdhocReadTestTwo(Test test) throws AdhocDBException {

        ResultSet result = null;

        try {
            long start = System.nanoTime();
            result = adHocReadTestTwo.executeQuery();

            /* For verification purposes
            while (result.next()) {
                System.out.println(result.getInt("count"));
            }
             */

            long end = System.nanoTime();
            test.addExecutionTime(end - start);

        } catch (SQLException e) {
            handleException(getFailureMessage(test), e);
        } finally {
            closeResultSet(getFailureMessage(test), result);
        }
    }


    public void runAdhocReadTestThree(Test test) throws AdhocDBException {

        ResultSet result = null;

        try {
            long start = System.nanoTime();
            result = adHocReadTestThree.executeQuery();

            /* For verification purposes only
            while (result.next()) {
                System.out.println(result.getInt("count"));
            }

             */

            long end = System.nanoTime();
            test.addExecutionTime(end - start);

        } catch (SQLException e) {
            handleException(getFailureMessage(test), e);
        } finally {
            closeResultSet(getFailureMessage(test), result);
        }
    }


    public void runAdhocWriteTestOne(Test test) throws AdhocDBException {

        List<AdhocIndividual> individuals = new ArrayList<>();
        int updatedRows = 0;

        try {
            long start = System.nanoTime();
            updatedRows = adHocWriteTestOne.executeUpdate();
            connection.commit();
            long end = System.nanoTime();
            test.addExecutionTime(end - start);

        } catch (SQLException e) {
            handleException(getFailureMessage(test), e);
        }

        // Restore conditions
        try {
            updatedRows = resetAdhocWriteTestOne.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            handleException(getFailureMessage(test), e);
        }
    }

    public void runAdhocUpdateTestOne(Test test) throws AdhocDBException {

        List<AdhocIndividual> individuals = new ArrayList<>();
        int updatedRows = 0;

        // Prepare test and conditions
        try {
            updatedRows = adHocWriteTestOne.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            handleException(getFailureMessage(test), e);
        }

        // Run test
        try {
            long start = System.nanoTime();
            updatedRows = adhocUpdateTestOne.executeUpdate();
            connection.commit();
            long end = System.nanoTime();
            test.addExecutionTime(end - start);

            // Restore conditions
            updatedRows = resetAdhocWriteTestOne.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            handleException(getFailureMessage(test), e);
        }



    }

    private String getFailureMessage(Test test) {
        return "Failed to run" + test.getType() + "(#" +test.getTestNumber() + ") on " + test.getDb() + " database.";
    }
}
