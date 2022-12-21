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
    private final String DB_PATH = "test_normalized";
    private Connection connection;

    // Unique values for testing
    private static final String UNIQUE_PERSONAL_NUMBER = "20000101-1234";
    private static final String UNIQUE_FUND_ISIN_NUMBER = "SE0018539999";
    private static final String UNIQUE_FUND_NAME = "Eriks fond";
    private static final String UNIQUE_COMPANY_VAT_NR = "3470000000021";
    private static final String UNIQUE_COMPANY_NAME = "Deduce It Consulting AB";
    private static final String SET_FOCUS = "Hållbarhetsfokuserad";


    // TABLE NAMES

    private final String INVESTMENT_TABLE_NAME = "public.investment";
    private final String INSURANCE_PACKAGE_TABLE_NAME = "public.insurance_package";
    private final String PERSON_TABLE_NAME = "public.person";
    private final String OTHER_INVESTMENTS_TABLE_NAME = "public.other_investments";
    private final String FUND_TABLE_NAME = "public.fund";
    private final String COMPANY_TABLE_NAME = "public.company";

    // COLUMN NAMES
    private final String INVESTED_CAPITAL_COL_NAME = "invested_capital";
    private final String PERSON_ID_COL_NAME = "person_id";
    private final String TRAD_CAPITAL_COL_NAME = "trad_capital";
    private final String PERSON_NUMBER_COL_NAME = "person_number";
    private final String FIRST_NAME_COL_NAME = "first_name";
    private final String LAST_NAME_COL_NAME = "last_name";
    private final String ID_COL_NAME = "id";
    private final String ISIN_COL_NAME = "isin";
    private final String NAME_COL_NAME = "name";
    private final String INSURANCE_PACKAGE_ID = "insurance_package_id";
    private final String SUSTAINABILITY_FOCUS_COL_NAME = "sustainability_focus";
    private final String VAT_NR_COL_NAME = "vat_nr";
    private final String COMPANY_NAME_COL_NAME = "registered_name";


    // PREPARED STATEMENTS

    // CREATE TESTS
    private PreparedStatement normWriteTestOne;
    private PreparedStatement resetNormWriteTestOne;

    private PreparedStatement normWriteTestTwo;
    private PreparedStatement resetNormWriteTestTwo;

    // READ TESTS

    private PreparedStatement normReadTestOne;
    private PreparedStatement normReadTestTwo;
    private PreparedStatement normReadTestThree;
    private PreparedStatement normReadTestFour;

    // UPDATE TESTS
    private PreparedStatement prepareNormUpdateTestOne;
    private PreparedStatement normUpdateTestOne;
    private PreparedStatement resetNormUpdateTestOne;
    private PreparedStatement prepareNormUpdateTestTwo;
    private PreparedStatement normUpdateTestTwo;
    private PreparedStatement resetNormUpdateTestTwo;

    // DELETE TESTS
    private PreparedStatement prepareNormDeleteTestOne;
    private PreparedStatement normDeleteTestOne;
    private PreparedStatement prepareNormDeleteTestTwo;
    private PreparedStatement normDeleteTestTwo;




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
        normReadTestTwo = connection.prepareStatement(
                "SELECT COUNT (1) " +
                        "FROM (SELECT SUM(" + INVESTED_CAPITAL_COL_NAME + ")" + " AS totalCapital, " +
                        "IP." + PERSON_ID_COL_NAME + " " +
                        "FROM " + INVESTMENT_TABLE_NAME + " AS I INNER JOIN " +
                        INSURANCE_PACKAGE_TABLE_NAME + " AS IP ON (I." + INSURANCE_PACKAGE_ID + " = IP.id) " +
                        "GROUP BY IP." + PERSON_ID_COL_NAME + " " +
                        "HAVING SUM(" + INVESTED_CAPITAL_COL_NAME + ") > 50000) AS T " +
                        "INNER JOIN " + PERSON_TABLE_NAME + " AS P ON (T." + PERSON_ID_COL_NAME + " = P." + ID_COL_NAME + ") "
        );
        normReadTestThree = connection.prepareStatement(
                "SELECT COUNT (*) " +
                        "FROM " + OTHER_INVESTMENTS_TABLE_NAME + " " +
                        "WHERE " + TRAD_CAPITAL_COL_NAME + " > 45000"
        );
        normReadTestFour = connection.prepareStatement(
                "SELECT COUNT (*) " +
                        "FROM " + PERSON_TABLE_NAME + " " +
                        "WHERE SUBSTRING(" + PERSON_NUMBER_COL_NAME + ", 5, 2) = '03'"
        );


        normUpdateTestOne = connection.prepareStatement(
        "UPDATE " + INVESTMENT_TABLE_NAME + " " +
                "SET " + INVESTED_CAPITAL_COL_NAME + " = 0 " +
                "WHERE " + ID_COL_NAME + " = 1"
        );

        prepareNormUpdateTestOne = connection.prepareStatement(
                "SELECT " + INVESTED_CAPITAL_COL_NAME + " " +
                        "FROM "+ INVESTMENT_TABLE_NAME + " " +
                        "WHERE id = 1"
        );
        resetNormUpdateTestOne = connection.prepareStatement(
                "UPDATE " + INVESTMENT_TABLE_NAME + " " +
                        "SET " + INVESTED_CAPITAL_COL_NAME + " = ? " + // invested capital
                        "WHERE id = 1"
        );

        prepareNormUpdateTestTwo = connection.prepareStatement(
                "INSERT INTO " + FUND_TABLE_NAME + " (" + ISIN_COL_NAME + ", " + NAME_COL_NAME + ") " +
                        "VALUES ('"+ UNIQUE_FUND_ISIN_NUMBER + "', '" + UNIQUE_FUND_NAME + "')"
        );

        normUpdateTestTwo = connection.prepareStatement(
                "UPDATE " + FUND_TABLE_NAME + "  " +
                        "SET " + SUSTAINABILITY_FOCUS_COL_NAME + " = '" + SET_FOCUS + "' " +
                        "WHERE " + ISIN_COL_NAME + " = '" + UNIQUE_FUND_ISIN_NUMBER + "'"
        );
        resetNormUpdateTestTwo = connection.prepareStatement(
                "DELETE FROM " + FUND_TABLE_NAME + " " +
                        "WHERE " + ISIN_COL_NAME + " = '" + UNIQUE_FUND_ISIN_NUMBER + "'"
        );

        normWriteTestOne = connection.prepareStatement(
                "INSERT INTO " + PERSON_TABLE_NAME + " (" + PERSON_NUMBER_COL_NAME + ", " + FIRST_NAME_COL_NAME + ", " + LAST_NAME_COL_NAME + ") " +
                        "VALUES ('" + UNIQUE_PERSONAL_NUMBER + "', 'Kalle', 'Johansson') "

        );
        resetNormWriteTestOne = connection.prepareStatement(
                "DELETE FROM " + PERSON_TABLE_NAME + " " +
                        "WHERE " + PERSON_NUMBER_COL_NAME + " = '" + UNIQUE_PERSONAL_NUMBER + "'"
        );

        normWriteTestTwo = connection.prepareStatement(
                "INSERT INTO " + COMPANY_TABLE_NAME + " (" + VAT_NR_COL_NAME + ", " + COMPANY_NAME_COL_NAME + ") " +
                        "VALUES ('" + UNIQUE_COMPANY_VAT_NR + "', '" + UNIQUE_COMPANY_NAME + "')"

        );
        resetNormWriteTestTwo = connection.prepareStatement(
                "DELETE FROM " + COMPANY_TABLE_NAME + " " +
                        "WHERE " + VAT_NR_COL_NAME + " = '" + UNIQUE_COMPANY_VAT_NR + "'"
        );
        prepareNormDeleteTestOne = normWriteTestOne;
        normDeleteTestOne = resetNormWriteTestOne;
        prepareNormDeleteTestTwo = normWriteTestTwo;
        normDeleteTestTwo = resetNormWriteTestTwo;

    }

    private void connectToNormDB() throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + DB_PATH,
                DB_USERNAME, DB_PASSWORD);
        connection.setAutoCommit(false);
    }

    public void runNormReadTestOne(Test test) throws NormDBException {

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
            handleException(getFailureMessage(test), e);
        } finally {
            closeResultSet(getFailureMessage(test), result);
        }

    }

    public void runNormReadTestTwo(Test test) throws NormDBException {

        ResultSet result = null;

        try {
            long start = System.nanoTime();
            result = normReadTestTwo.executeQuery();
            long end = System.nanoTime();
            test.addExecutionTime(end - start);

        } catch (SQLException e) {
            handleException(getFailureMessage(test), e);
        } finally {
            closeResultSet(getFailureMessage(test), result);
        }
    }

    public void runNormReadTestThree(Test test) throws NormDBException {

        ResultSet result = null;

        try {
            long start = System.nanoTime();
            result = normReadTestThree.executeQuery();
            long end = System.nanoTime();
            test.addExecutionTime(end - start);

        } catch (SQLException e) {
            handleException(getFailureMessage(test), e);
        } finally {
            closeResultSet(getFailureMessage(test), result);
        }
    }

    public void runNormReadTestFour(Test test) throws NormDBException {

        ResultSet result = null;

        try {
            long start = System.nanoTime();
            result = normReadTestFour.executeQuery();
            long end = System.nanoTime();
            test.addExecutionTime(end - start);

            /* For verification purposes
            while (result.next()) {
                System.out.println(result.getInt("count"));
            }

             */


        } catch (SQLException e) {
            handleException(getFailureMessage(test), e);
        } finally {
            closeResultSet(getFailureMessage(test), result);
        }
    }


    public void runNormWriteTestOne(Test test) throws NormDBException {

        int updatedRows = 0;

        try {
            long start = System.nanoTime();
            updatedRows = normWriteTestOne.executeUpdate();
            connection.commit();
            long end = System.nanoTime();
            test.addExecutionTime(end - start);

        } catch (SQLException e) {
            handleException(getFailureMessage(test), e);
        }

        // Restore conditions
        try {
            updatedRows = resetNormWriteTestOne.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            handleException(getFailureMessage(test), e);
        }
    }

    public void runNormWriteTestTwo(Test test) throws NormDBException {

        int updatedRows = 0;

        try {
            long start = System.nanoTime();
            updatedRows = normWriteTestTwo.executeUpdate();
            connection.commit();
            long end = System.nanoTime();
            test.addExecutionTime(end - start);

        } catch (SQLException e) {
            handleException(getFailureMessage(test), e);
        }

        // Restore conditions
        try {
            updatedRows = resetNormWriteTestTwo.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            handleException(getFailureMessage(test), e);
        }


    }

    public void runNormUpdateTestOne(Test test) throws NormDBException {

        ResultSet result = null;
        double originalValue = 0;
        int updatedRows = 0;

        // Prepare test and conditions
        try {
            result = prepareNormUpdateTestOne.executeQuery();

            while (result.next()) {
                originalValue = result.getDouble(INVESTED_CAPITAL_COL_NAME);
            }

        } catch (SQLException e) {
            handleException(getFailureMessage(test), e);
        } finally {
            closeResultSet(getFailureMessage(test), result);
        }

        try {
            // Perform test
            long start = System.nanoTime();
            updatedRows = normUpdateTestOne.executeUpdate();
            connection.commit();
            long end = System.nanoTime();
            test.addExecutionTime(end - start);

            // Restore conditions
            resetNormUpdateTestOne.setDouble(1,originalValue);
            updatedRows = resetNormUpdateTestOne.executeUpdate();
            connection.commit();


        } catch (SQLException e) {
            handleException(getFailureMessage(test), e);
        }


    }

    public void runNormUpdateTestTwo(Test test) throws NormDBException {


        double originalValue = 0;
        int updatedRows = 0;

        // Prepare test and conditions
        try {
            updatedRows = prepareNormUpdateTestTwo.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            handleException(getFailureMessage(test), e);
        }

        try {
            // Perform test
            long start = System.nanoTime();
            updatedRows = normUpdateTestTwo.executeUpdate();
            connection.commit();
            long end = System.nanoTime();
            test.addExecutionTime(end - start);

            // Restore conditions
            updatedRows = resetNormUpdateTestTwo.executeUpdate();
            connection.commit();


        } catch (SQLException e) {
            handleException(getFailureMessage(test), e);
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

    private String getFailureMessage(Test test) {
        return "Failed to run" + test.getType() + "(#" +test.getTestNumber() + ") on " + test.getDb() + " database.";
    }


    public void runNormDeleteTestOne(Test test) throws NormDBException {

        int updatedRows = 0;

        // Prepare test
        try {
            updatedRows = prepareNormDeleteTestOne.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            handleException(getFailureMessage(test), e);
        }

        // Run test
        try {
            long start = System.nanoTime();
            updatedRows = normDeleteTestOne.executeUpdate();
            connection.commit();
            long end = System.nanoTime();
            test.addExecutionTime(end - start);

        } catch (SQLException e) {
            handleException(getFailureMessage(test), e);
        }

    }

    public void runNormDeleteTestTwo(Test test) throws NormDBException {

        int updatedRows = 0;

        // Prepare test
        try {
            updatedRows = prepareNormDeleteTestTwo.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            handleException(getFailureMessage(test), e);
        }

        // Run test
        try {
            long start = System.nanoTime();
            updatedRows = normDeleteTestTwo.executeUpdate();
            connection.commit();
            long end = System.nanoTime();
            test.addExecutionTime(end - start);

        } catch (SQLException e) {
            handleException(getFailureMessage(test), e);
        }

    }
}
