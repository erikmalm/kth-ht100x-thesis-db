package se.experiment.view;

/**
 * Defines all commands that can be performed by a user of the chat application.
 */
public enum Command {

    /**
     * Runs a query to fetch all individuals from the ad hoc database.
     */
    GET,

    /**
     * Runs the experiment
     */
    TEST,

    /**
     * Lists all commands.
     */

    HELP,

    /**
     * Enables/Disables printing
     */
    PRINT,

    /**
     * Leave the test application.
     */
    QUIT,

    /**
     * None of the valid commands above was specified.
     */
    ILLEGAL_COMMAND
}