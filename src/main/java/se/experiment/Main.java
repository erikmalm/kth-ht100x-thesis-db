package se.experiment;

import se.experiment.controller.Controller;
import se.experiment.exceptions.AdhocDBException;
import se.experiment.exceptions.NormDBException;
import se.experiment.view.BlockingInterpreter;

public class Main {
    public static void main(String[] args) {

        try {
            new BlockingInterpreter(new Controller()).handleCmds();
        } catch (AdhocDBException | NormDBException e) {
            System.out.println("Could not connect to database." + e.getMessage());
            e.printStackTrace();
        }

        /*

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(e);
            System.exit(-1);
        }


        try {
            // open connection to database
            Connection connection = DriverManager.getConnection(
                    //"jdbc:postgresql://dbhost:port/dbname", "user", "dbpass");
                    "jdbc:postgresql://localhost:5432/adhoc", "postgres", "admin");

            // build query, here we get info about all databases"
            String query = "SELECT * FROM public.hallbarheter_privat_id";

            // execute query
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            // return query result
            while (rs.next())
                // display table name
                System.out.println("PostgreSQL Query result: " + rs.getString("id"));
            connection.close();
        } catch (java.sql.SQLException e) {
            System.err.println(e);
            System.exit(-1);
        }

         */


    }
}