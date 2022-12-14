package se.experiment.integration;

import se.experiment.exceptions.AdhocDBException;
import se.experiment.model.AdhocIndividual;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdhocDAO {

    // DB Setup
    private final String DB_USERNAME = "postgres";
    private final String DB_PASSWORD = "adnin";
    private Connection connection;

    // TABLES
    private static final String INDIVIDUALS_TABLE_NAME = "public.hallbarheter_privat_id";

    // COLUMNS
    private static final String ID_COL_NAME = "id";
    private static final String PERSONAL_NUMBER_COL_NAME = "person_id";

    //STATEMENTS
    private PreparedStatement getAllAdhocIndividuals;

    private void prepareStatements() throws SQLException {

        getAllAdhocIndividuals = connection.prepareStatement(
                "SELECT * FROM " + INDIVIDUALS_TABLE_NAME
        );
    }

    public AdhocDAO() throws AdhocDBException {
        System.out.println("Setting up AdhocDAO");
        try {
            connectToAdhocDB();
            prepareStatements();
        } catch (ClassNotFoundException | SQLException e) {
            throw new AdhocDBException("Could not connect to database", e);
        }
    }

    private void connectToAdhocDB() throws ClassNotFoundException, SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/adhoc",
                "postgres", "admin");
        connection.setAutoCommit(false);
    }

    public List<AdhocIndividual> getAllIndividuals() throws AdhocDBException {

        String failureMessage = "Unable to get all individuals";
        List<AdhocIndividual> individuals = new ArrayList<>();
        ResultSet result = null;

        try {
            result = getAllAdhocIndividuals.executeQuery();
            individuals = new ArrayList<AdhocIndividual>();

            while (result.next()) {
                individuals.add(new AdhocIndividual(
                        result.getString(ID_COL_NAME),
                        result.getString(PERSONAL_NUMBER_COL_NAME))
                );

            }

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


}
