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

    /**
     * Creates a new instance that will use the specified controller for all operations.
     *
     * @param controller The controller used by this instance.
     */
    public BlockingInterpreter(Controller controller) {
        this.controller = controller;
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

        String table = "";

        while (keepReceivingCmds) {
            try {
                CmdLine cmdLine = new CmdLine(readNextLine());
                switch (cmdLine.getCmd()) {

                    case HELP:
                        for (Command command : Command.values()) {
                            if (command == Command.ILLEGAL_COMMAND) {
                                continue;
                            }
                            System.out.println(command.toString().toLowerCase());
                        }
                        break;

                    case GET:

                        table = cmdLine.getParameter(0);

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

                    case RENT:

//

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



    private String readNextLine() {
        System.out.print(PROMPT);
        return console.nextLine();
    }
}