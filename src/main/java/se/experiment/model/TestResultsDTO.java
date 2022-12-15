package se.experiment.model;

import java.util.List;

public interface TestResultsDTO {

    public List<Integer> getListOfExecutionTimes();

    public String getType();

    public int getAmountOfTests();

    public String toString();

}
