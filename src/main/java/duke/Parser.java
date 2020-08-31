package duke;

import java.util.zip.DataFormatException;

import duke.command.AddCommand;
import duke.command.ByeCommand;
import duke.command.Command;
import duke.command.DeleteCommand;
import duke.command.DoneCommand;
import duke.command.ListCommand;


/**
 * Understands inputs given by user and validates them.
 */
public class Parser {

    /**
     * Understands input from user
     *
     * @param userText input from user
     * @param taskList list of tasks user has currently
     * @return boolean value of whether conversation is to be continued
     */
    public static boolean understandText(String userText, TaskList taskList) {
        try {
            String edittedAnswer = userText.strip().toLowerCase();
            String[] answers = userText.split(" ");
            if (answers.length == 2 && answers[0].equals(Commands.DONE.text)) {
                Parser.furtherUnderstandTaskNumber(answers[0], answers[1], taskList);

            } else if (answers.length == 2 && answers[0].equals(Commands.DELETE.text)) {
                Command command = new DeleteCommand();
                Parser.furtherUnderstandTaskNumber(answers[0], answers[1], taskList);

            } else if (edittedAnswer.equals(Commands.LIST.text)) {
                Command command = new ListCommand();
                command.execute(" ", taskList);

            } else if (edittedAnswer.equals(Commands.EXIT.text)) {
                Command command = new ByeCommand();
                command.execute(" ", taskList);
                return false;

            } else if (answers[0].equals(Commands.TODO.text)
                    || answers[0].equals(Commands.DEADLINE.text)
                    || answers[0].equals(Commands.EVENT.text)) {
                Parser.furtherUnderstandTask(userText, taskList);

            } else {
                throw new DukeCannotUnderstandException();

            }
        } catch (DukeCannotUnderstandException e) {
            System.out.println(e.getMessage() + "\n" + Ui.LINE);
        }
        return true;
    }

    private static void furtherUnderstandTaskNumber(String stringCommand, String answer, TaskList taskList) {
        try {
            int oneIndex = Integer.parseInt(answer);
            int realIndex = oneIndex - 1;
            Command command;
            if (stringCommand.equals(Commands.DONE.text)) {
                command = new DoneCommand();
            } else {
                command = new DeleteCommand();
            }
            command.execute(String.valueOf(realIndex), taskList);
        } catch (NumberFormatException e) {
            System.out.println("I can't seem to understand what task you are referring to.\n"
                    + "Please let me know in this format: " + stringCommand
                    + " <number of task>\n" + Ui.LINE);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Hmm... I don't have a task numbered " + answer + "\n" + Ui.LINE);
        }
    }

    private static void furtherUnderstandTask(String answer, TaskList taskList) {
        try {
            String[] answers = answer.split(" ", 2);
            if (answers.length > 1) {
                String type = answers[0].strip();
                String task = answers[1].strip();
                String[] partsOfTask = task.split("/");

                // checking for event and deadline if the date is given or not
                if (type.equals(Commands.TODO.text) || partsOfTask.length == 2) {
                    if (type.equals(Commands.TODO.text)) {
                        Command command = new AddCommand(type);
                        command.execute(task, taskList);

                    } else {
                        String description = partsOfTask[0].strip();
                        String date = partsOfTask[1].strip();

                        // check if the date is given in correct format
                        if (date.length() != 8) {
                            throw new DataFormatException();
                        }

                        Command command = new AddCommand(type);
                        command.execute(task, taskList);

                    }
                } else {
                    String instruction = "<type of task> <description> / <deadline>";
                    if (type.equals(Commands.EVENT.text)) {
                        instruction = "<type of task> <description> / <date of event>";
                    }
                    throw new DukeGotNoArgumentsException(instruction);
                }
            } else {
                // handles the case when no description
                String instruction = "<type of task> <description>";
                if (answers[0].equals(Commands.DEADLINE.text)) {
                    instruction = "<type of task> <description> / <due date>";
                } else if (answers[0].equals(Commands.EVENT.text)) {
                    instruction = "<type of task> <description> / <date of event>";
                }
                throw new DukeGotNoArgumentsException(instruction);
            }
        } catch (DukeGotNoArgumentsException e) {
            System.out.println(e.getMessage() + "\n" + Ui.LINE);
        } catch (DataFormatException e) {
            System.out.println("Please key in again with the date in the ddmmyyyy format." + "\n" + Ui.LINE);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(answer);
        }
    }

    enum Commands {
        TODO("todo"),
        DEADLINE("deadline"),
        EVENT("event"),
        LIST("list"),
        EXIT("bye"),
        DONE("done"),
        DELETE("delete");
        private String text;

        Commands(String text) {
            this.text = text;
        }
    }

}