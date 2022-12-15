package se.experiment.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Test implements TestDTO {


    private static String type;
    private static String db;
    private static int testNumber;
    private static int amountOfRuns;

    public List<Long> listOfExecutionTimes;


    public Test(String type, String db, int testNumber, int amountOfRuns) {
        this.type = type;
        this.db = db;
        this.amountOfRuns = amountOfRuns;
        this.testNumber = testNumber;
        this.listOfExecutionTimes = new ArrayList<>();

    }

    @Override
    public List<Long> getListOfExecutionTimes() {
        return listOfExecutionTimes;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getDb() {
        return db;
    }

    @Override
    public int getTestNumber() {
        return testNumber;
    }

    @Override
    public int getAmountOfTests() {
        return amountOfRuns;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        int pos = 1;

        sb.append("{");

        for (Long l : listOfExecutionTimes) {
            sb.append(l);

            if (pos++ != amountOfRuns) sb.append(", ");
            else sb.append("}");
        }

        return sb.toString();
    }

    public void addExecutionTime(long l) {
        listOfExecutionTimes.add(l);
    }
}
