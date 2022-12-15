package se.experiment.view;


import se.experiment.controller.Controller;
import se.experiment.exceptions.AdhocDBException;
import se.experiment.model.Test;
import se.experiment.model.IndividualDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class BlockingInterpreter {
    private static final String PROMPT = "> ";
    private final Scanner console = new Scanner(System.in);
    private Controller controller;
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

//

                        break;

                    case TEST:

                        // VARIABLES FOR TEST RUN
                        String dbs = cmdLine.getParameter(0);
                        String typ = cmdLine.getParameter(1);
                        String prm = cmdLine.getParameter(2);
                        String amt = cmdLine.getParameter(3);

                        if (ifNullOrEmpty(dbs, typ)) {
                            out(errorMessageForRun);
                            break;
                        }

                        int tstNumber = 1;
                        int tstAmount = 1;

                        if (prm != null) {
                            tstNumber = Integer.parseInt(prm);
                        }

                        if (amt != null) {
                            tstAmount = Integer.parseInt(amt);
                        }

                        Test current = new Test(typ, dbs, tstNumber, tstAmount);

                        parseTestCommand(current);

                        endTest(current);


                        break;

                    case FIND:

//

                        break;

                    case END:

//

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



    private void parseTestCommand(Test test) {

        System.out.println("Setup test on " + test.getDb() + " on operation " + test.getType() + ", " + test.getAmountOfTests() + " number of times");

        switch(test.getDb()) {
            case "ADHOC":
                runAdhocTests(test);
                break;
            default:
                out(errorMessageForParseTestCommand);
                break;
        }
    }

    private void runAdhocTests(Test test) {
        switch(test.getType()) {
            case "READ":
                runAdhocReadTest(test);
                break;

            case "WRITE":
                runAdhocWriteTest(test);
                break;

            case "UPDATE":
                runAdhocUpdateTest(test);
                break;

            default:
                break;
        }
    }

    private void runAdhocUpdateTest(Test test) {
        switch (test.getTestNumber()) {
            case 1:
                try {
                    for (int i = 0; i < test.getAmountOfTests(); i ++)
                        controller.runAdhocUpdateTestOne(test);
                } catch (AdhocDBException e) {
                    System.out.println("Test failed " + e.getMessage());
                }
                break;

            default:
                break;
        }
    }

    private void runAdhocWriteTest(Test test) {
        switch (test.getTestNumber()) {
            case 1:
                try {
                    for (int i = 0; i < test.getAmountOfTests(); i ++)
                        controller.runAdhocWriteTestOne(test);
                } catch (AdhocDBException e) {
                    System.out.println("Test failed " + e.getMessage());
                }
                break;

            default:
                break;
        }
    }

    private void runAdhocReadTest(Test test)  {
        switch(test.getTestNumber()) {
            case 1:
                try {
                    for (int i = 0; i < test.getAmountOfTests(); i ++)
                        controller.runAdhocReadTestOne(test);
                } catch (AdhocDBException e) {
                    System.out.println("Test failed " + e.getMessage());
                }
                break;

            default:
                break;
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