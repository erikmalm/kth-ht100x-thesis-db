package se.experiment.model;

import java.util.List;
import java.util.SimpleTimeZone;

public class AdhocTestResults implements TestResultsDTO {

    public List<Integer> listOfExecutionTimes;

    @Override
    public List<Integer> getListOfExecutionTimes() {
        return listOfExecutionTimes;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public int getAmountOfTests() {
        return 0;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        for (Integer i : listOfExecutionTimes)
            sb.append(i);

        return sb.toString();
    }
}
