package se.experiment.integration;

import se.experiment.exceptions.AdhocDBException;
import se.experiment.exceptions.NormDBException;
import se.experiment.model.AdhocIndividual;
import se.experiment.model.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NormDAO {

    private final String DB_USERNAME = "postgres";
    private final String DB_PASSWORD = "admin";
    private Connection connection;

    private final String INVESTMENT_TABLE_NAME = "public.investment";
    private final String INSURANCE_PACKAGE_TABLE_NAME = "public.insurance_package";
    private final String PERSON_TABLE_NAME = "public.person";


    private final String INVESTED_CAPITAL_COL_NAME = "invested_capital";
    private final String PERSON_ID_COL_NAME = "person_id";
    private final String ID_COL_NAME = "id";


    private final String INSURANCE_PACKAGE_ID = "insurance_package_id";

    private PreparedStatement normReadTestOne;



    public NormDAO() throws NormDBException {
        System.out.println("Setting up NormDAO");
        try {
            connectToNormDB();
            prepareStatements();
        } catch (SQLException e) {
            throw new NormDBException("Could not connect to database", e);
        }
    }

    private void prepareStatements() throws SQLException {

        normReadTestOne = connection.prepareStatement(
                "SELECT * " +
                        "FROM (SELECT SUM(" + INVESTED_CAPITAL_COL_NAME + ")" + " AS totalCapital, " +
                        "IP." + PERSON_ID_COL_NAME + " " +
                        "FROM " + INVESTMENT_TABLE_NAME + " AS I INNER JOIN " +
                        INSURANCE_PACKAGE_TABLE_NAME + " AS IP ON (I." + INSURANCE_PACKAGE_ID + " = IP.id) " +
                        "GROUP BY IP." + PERSON_ID_COL_NAME + ") AS T " +
                        "INNER JOIN " + PERSON_TABLE_NAME + " AS P ON (T." + PERSON_ID_COL_NAME + " = P." + ID_COL_NAME + ") " +
                        "ORDER BY T.totalCapital " +
                        "LIMIT 1"
        );

    }

    private void connectToNormDB() throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/normalized",
                DB_USERNAME, DB_PASSWORD);
        connection.setAutoCommit(false);
    }

    public void runNormReadTestOne(Test test) throws NormDBException {

        String failureMessage = "Failed to run READ test number one on Adhoc Database";
        List<AdhocIndividual> individuals = new ArrayList<>();
        ResultSet result = null;

       // System.out.println(normReadTestOne);

        try {
            long start = System.nanoTime();
            result = normReadTestOne.executeQuery();
            individuals = new ArrayList<AdhocIndividual>();

            while (result.next()) {
                individuals.add(new AdhocIndividual(
                        result.getString(ID_COL_NAME),
                        result.getString(PERSON_ID_COL_NAME))
                );
            }

            //System.out.println(individuals);

            long end = System.nanoTime();
            test.addExecutionTime(end - start);

        } catch (SQLException e) {
            handleException(failureMessage, e);
        } finally {
            closeResultSet(failureMessage, result);
        }


    }

    private void closeResultSet(String failureMessage, ResultSet result) throws NormDBException {
        try {
            result.close();
        } catch (Exception e) {
            throw new NormDBException(failureMessage + " Could not close result set.", e);
        }
    }

    private void handleException(String failureMsg, SQLException cause) throws NormDBException {
        String completeFailureMsg = failureMsg;
        try {
            connection.rollback();
        } catch (SQLException rollbackExc) {
            completeFailureMsg = completeFailureMsg +
                    ". Also failed to rollback query because of: " + rollbackExc.getMessage();
        }

        if (cause != null) {
            throw new NormDBException(failureMsg, cause);
        } else {
            throw new NormDBException(failureMsg);
        }
    }
}
