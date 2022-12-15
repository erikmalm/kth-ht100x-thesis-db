package se.experiment.model;

import java.util.List;

public interface TestDTO {

    public List<Long> getListOfExecutionTimes();

    public String getType();

    public int getAmountOfTests();

    public String toString();

    public String getDb();

    public int getTestNumber();


}
