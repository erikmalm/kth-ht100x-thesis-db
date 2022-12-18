package se.experiment.view;


import se.experiment.controller.Controller;
import se.experiment.exceptions.AdhocDBException;
import se.experiment.exceptions.NormDBException;
import se.experiment.model.Test;
import se.experiment.model.IndividualDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class BlockingInterpreter {
    private static final String PROMPT = "> ";
    private final Scanner console = new Scanner(System.in);
    private final Controller controller;
    private boolean keepReceivingCmds = false;

    private boolean shouldPrintResults = true;


    // DEFAULT MESSAGES

    String errorMessageForRun;
    String errorMessageForParseTestCommand;

    /**
     * Creates a new instance that will use the specified controller for all operations.
     *
     * @param controller The controller used by this instance.
     */
    public BlockingInterpreter(Controller controller) {
        this.controller = controller;
        setupMessages();
    }

    private void setupMessages() {
        errorMessageForRun =
                "Error: Command should be on format:\n" +
                        "TEST [DB] [READ] <OPTIONAL: TEST NUMBER> <OPTIONAL: AMOUNT>\n" +
                        "[NOTE] Test number and test amount are optional. Default test and amount will be number 1.\n" +
                        "[EXAMPLE] RUN ADHOC READ\n" +
                        "[EXAMPLE] RUN ADHOC READ 1 10";

        errorMessageForParseTestCommand =
                "Error: Missing correct input for test command. Command should be on format:\n" +
                        "TEST [DB] [READ] <OPTIONAL: NUMBER>\n" +
                        "[NOTE] Incorrect database [DB] in test command.\n" +
                        "[NOTE] Available databases are: ADHOC, NORM";
    }

    /**
     * Stops the commend interpreter.
     */
    public void stop() {
        keepReceivingCmds = false;
    }

    /**
     * Interprets and performs user commands. This method will not return until the
     * UI has been stopped. The UI is stopped when the user gives the
     * "quit" command.
     */
    public void handleCmds() {

        keepReceivingCmds = true;

        while (keepReceivingCmds) {
            try {
                CmdLine cmdLine = new CmdLine(readNextLine());
                switch (cmdLine.getCmd()) {

                    case HELP:
                        for (Command command : Command.values()) {
                            if (command == Command.ILLEGAL_COMMAND) {
                                continue;
                            }
                            System.out.println(command.toString().toUpperCase());
                        }
                        break;

                    case GET:

                        String table = cmdLine.getParameter(0);

                        if (Objects.equals(table, "")) {
                            System.out.println(table);
                            System.out.println("You need to specify a table to get");

                        }
                        else{

                            try {
                                List<? extends IndividualDTO> result = controller.getAllAdhocIndividuals();

                                for (IndividualDTO i : result) {
                                    System.out.println(i);
                                }
                            }
                            catch (SQLException e){
                                System.out.println(e.getMessage());
                            }
                        }

                        // Print the result

                        // Reset values

                        break;



                    case PRINT:
                        shouldPrintResults = !shouldPrintResults;
                        if (shouldPrintResults) out("PRINT: ON");
                        else out("PRINT: OFF");

                        break;

                    case TEST:

                        // VARIABLES FOR TEST RUN
                        String dbs = cmdLine.getParameter(0);
                        String typ = cmdLine.getParameter(1);
                        String prm = cmdLine.getParameter(2);
                        String amt = cmdLine.getParameter(3);

                        // Check for critical input
                        if (ifNullOrEmpty(dbs, typ)) {
                            out(errorMessageForRun);
                            break;
                        }

                        // Process optional input
                        int tstNumber = getInputNumber(prm);
                        int tstAmount = getInputNumber(amt);

                        // Setup and execute test
                        Test current = new Test(typ, dbs, tstNumber, tstAmount);
                        parseTestCommand(current);
                        System.out.println("Test complete.");
                        endTest(current);

                        break;

                    case FIND:
                        out("Not implemented.");
                        break;

                    case END:
                        out("Not implemented.");
                        break;

                    case QUIT:
                        keepReceivingCmds = false;
                        break;

                    default:
                        System.out.println("illegal command");
                }
            } catch (Exception e) {
                System.out.println("Operation failed");
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static int getInputNumber(String var) {
        // If no input, default to 1
        int ipt = 1;

        // If input, set that numbeer
        if (var != null) {
            ipt = Integer.parseInt(var);
        }

        // Return value
        return ipt;
    }


    private void parseTestCommand(Test test) {

        if (hasValidInput(test))
            printTestDetails(test);

        switch (test.getDb()) {
            case "ADHOC" -> runAdhocTests(test);
            case "NORM" -> runNormTests(test);
            default -> out(errorMessageForParseTestCommand);
        }
    }

    private boolean hasValidInput(Test test) {
        return (Objects.equals(test.getDb(), "ADHOC") || Objects.equals(test.getDb(), "NORM")) &&
                Objects.equals(test.getType(), "READ") || Objects.equals(test.getType(), "WRITE") || Objects.equals(test.getType(), "UPDATE");
    }

    private void printTestDetails(Test test) {
        System.out.println("Setup " + test.getType() + " (#" + test.getTestNumber() + ") test on " + test.getDb() + ", " + test.getAmountOfTests() + " number of times");
        System.out.println("Running test, please wait...");
    }

    private void runNormTests(Test test) {
        switch (test.getType()) {
            case "READ" -> runNormReadTest(test);
            case "WRITE" -> runNormWriteTest(test);
            case "UPDATE" -> runNormUpdateTest(test);
            default -> {
            }
        }
    }

    private void runNormUpdateTest(Test test) {
        switch(test.getTestNumber()) {
            case 1 -> {
                try {
                    for (int i = 0; i < test.getAmountOfTests(); i++)
                        controller.runNormUpdateTestOne(test);
                } catch (NormDBException e) {
                    System.out.println("Test failed " + e.getMessage());
                }
            }
            case 2 -> {
                try {
                    for (int i = 0; i < test.getAmountOfTests(); i++)
                        controller.runNormUpdateTestTwo(test);
                } catch (NormDBException e) {
                    System.out.println("Test failed " + e.getMessage());
                }
            }
        }
    }

    private void runNormWriteTest(Test test) {
        switch (test.getTestNumber()) {
            case 1 -> {
                try {
                    for (int i = 0; i < test.getAmountOfTests(); i++)
                        controller.runNormWriteTestOne(test);
                } catch (NormDBException e) {
                    System.out.println("Test failed " + e.getMessage());
                }
            }
            case 2 -> {
                try {
                    for (int i = 0; i < test.getAmountOfTests(); i++)
                        controller.runNormWriteTestTwo(test);
                } catch (NormDBException e) {
                    System.out.println("Test failed " + e.getMessage());
                }
            }
        }
    }

    private void runNormReadTest(Test test) {
        switch (test.getTestNumber()) {
            case 1 -> {
                try {
                    for (int i = 0; i < test.getAmountOfTests(); i++)
                        controller.runNormReadTestOne(test);
                } catch (NormDBException e) {
                    System.out.println("Test failed " + e.getMessage());
                }
            }
            case 2 -> {
                try {
                    for (int i = 0; i < test.getAmountOfTests(); i++)
                        controller.runNormReadTestTwo(test);
                } catch (NormDBException e) {
                    System.out.println("Test failed " + e.getMessage());
                }
            }
            case 3 -> {
                try {
                    for (int i = 0; i < test.getAmountOfTests(); i++)
                        controller.runNormReadTestThree(test);
                } catch (NormDBException e) {
                    System.out.println("Test failed " + e.getMessage());
                }
            }
            case 4 -> {
                try {
                    for (int i = 0; i < test.getAmountOfTests(); i++)
                        controller.runNormReadTestFour(test);
                } catch (NormDBException e) {
                    System.out.println("Test failed " + e.getMessage());
                }
            }
        }
    }

    private void runAdhocTests(Test test) {
        switch (test.getType()) {
            case "READ" -> runAdhocReadTest(test);
            case "WRITE" -> runAdhocWriteTest(test);
            case "UPDATE" -> runAdhocUpdateTest(test);
            default -> {
            }
        }
    }

    private void runAdhocUpdateTest(Test test) {
        switch (test.getTestNumber()) {
            case 1 -> {
                try {
                    for (int i = 0; i < test.getAmountOfTests(); i++)
                        controller.runAdhocUpdateTestOne(test);
                } catch (AdhocDBException e) {
                    System.out.println("Test failed " + e.getMessage());
                }
            }
            case 2 -> {
                try {
                    for (int i = 0; i < test.getAmountOfTests(); i++)
                        controller.runAdhocUpdateTestTwo(test);
                } catch (AdhocDBException e) {
                    System.out.println("Test failed " + e.getMessage());
                }
            }
        }
    }

    private void runAdhocWriteTest(Test test) {
        switch (test.getTestNumber()) {
            case 1 -> {
                try {
                    for (int i = 0; i < test.getAmountOfTests(); i++)
                        controller.runAdhocWriteTestOne(test);
                } catch (AdhocDBException e) {
                    System.out.println("Test failed " + e.getMessage());
                }
            }
            case 2 -> {
                try {
                    for (int i = 0; i < test.getAmountOfTests(); i++)
                        controller.runAdhocWriteTestTwo(test);
                } catch (AdhocDBException e) {
                    System.out.println("Test failed " + e.getMessage());
                }
            }
        }
    }

    private void runAdhocReadTest(Test test)  {
        switch (test.getTestNumber()) {
            case 1 -> {
                try {
                    for (int i = 0; i < test.getAmountOfTests(); i++)
                        controller.runAdhocReadTestOne(test);
                } catch (AdhocDBException e) {
                    System.out.println("Test failed " + e.getMessage());
                }
            }
            case 2 -> {
                try {
                    for (int i = 0; i < test.getAmountOfTests(); i++)
                        controller.runAdhocReadTestTwo(test);
                } catch (AdhocDBException e) {
                    System.out.println("Test failed " + e.getMessage());
                }
            }
            case 3 -> {
                try {
                    for (int i = 0; i < test.getAmountOfTests(); i++)
                        controller.runAdhocReadTestThree(test);
                } catch (AdhocDBException e) {
                    System.out.println("Test failed " + e.getMessage());
                }
            }
            case 4 -> {
                try {
                    for (int i = 0; i < test.getAmountOfTests(); i++)
                        controller.runAdhocReadTestFour(test);
                } catch (AdhocDBException e) {
                    System.out.println("Test failed " + e.getMessage());
                }
            }
        }
    }

    private boolean ifNullOrEmpty(String db, String type) {
        return db == null || type == null || Objects.equals(db,"") || Objects.equals(type,"");
    }

    private void out(String message) {
        System.out.println(message);
    }

    private void endTest(Test test) {
        if (shouldPrintResults) System.out.println(test);
        test = null;
    }


    private String readNextLine() {
        System.out.print(PROMPT);
        return console.nextLine();
    }
}