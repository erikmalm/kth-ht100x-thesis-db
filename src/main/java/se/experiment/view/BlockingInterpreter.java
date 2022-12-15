package se.experiment.view;


import se.experiment.controller.Controller;
import se.experiment.model.AdhocIndividual;
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

    // DEFAULT MESSAGES

    String errorMessageForRun;

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
                "Command should be on format RUN [DB] [READ] <OPTIONAL: NUMBER>\n" +
                "[NOTE] Test number is optional. Default test will be number 1.\n" +
                "[EXAMPLE] RUN ADHOC READ\n" +
                "[EXAMPLE] RUN ADHOC READ 2";
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



                    case AVAILABLE:

//

                        break;

                    case RUN:

                        // VARIABLES FOR TEST RUN
                        String db = cmdLine.getParameter(0);
                        String type =  cmdLine.getParameter(1);
                        String param = cmdLine.getParameter(2);

                        if (ifNullOrEmpty(db, type)) {
                            out(errorMessageForRun);
                            break;
                        }

                        int testNumber = 1;

                        if (param != null) {
                            testNumber = Integer.parseInt(param);
                        }





                        System.out.println("Run test on: " + db + " \tType of test: " + type);//

                        System.out.println("Test number: " + testNumber);

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

    private boolean ifNullOrEmpty(String db, String type) {
        return db == null || type == null || Objects.equals(db,"") || Objects.equals(type,"");
    }

    private void out(String errorMessage) {
        System.out.print("Error: ");
        System.out.println(errorMessage);
    }


    private String readNextLine() {
        System.out.print(PROMPT);
        return console.nextLine();
    }
}