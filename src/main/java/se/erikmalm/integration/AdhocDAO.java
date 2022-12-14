package se.erikmalm.integration;

import se.erikmalm.exceptions.AdhocDBException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

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

    public AdhocDAO() throws AdhocDBException {
        System.out.println("Setting up AdhocDAO");
        try {
            connectToAdhocDB();
        } catch (ClassNotFoundException | SQLException e) {
            throw new AdhocDBException("Could not connect to database", e);
        }
    }

    private void connectToAdhocDB() throws ClassNotFoundException, SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/adhoc",
                "postgres", "VgS4HN");
        connection.setAutoCommit(false);
    }


}
