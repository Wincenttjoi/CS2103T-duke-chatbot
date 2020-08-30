package duke;

import duke.exceptions.DukeException;
import duke.exceptions.DukeUnknownCommandException;

import java.io.IOException;

import java.time.format.DateTimeParseException;

/**
 * duke.Duke Class is the main class that will run based on different commands
 * given by user. Available commands include todo, deadline, event,
 * done, delete. duke.Todo, deadline and event are different types of tasks command.
 * Done and delete are commands to mark the list item as done or to
 * delete it respectively.
 * duke.Todo, deadline and event are followed by a description or message.
 * Eg: todo do CS2103T project
 * This would translate to a todo list item added into the user's overall list.
 * duke.Event and duke.Deadline would require /at and /by to specify the timing.
 * Description of done and delete would be a number to specify which
 * item in the list that should be marked as done or deleted.
 */
public class Duke {

    private Ui ui;
    private Storage storage;
    private TaskList taskList;

    public Duke() {
        ui = new Ui();
        storage = new Storage("data/tasks.txt");
        taskList = new TaskList();
    }

    /**
     * Constructor for Duke.
     */
    public Duke(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        taskList = new TaskList();
    }

    /**
     * Invoke run for duke chatbot programme.
     *
     * @param args argument.
     * @throws IOException if file does not exist.
     */
    public static void main(String[] args) throws IOException {
        new Duke("data/tasks.txt").run();
    }

    /**
     * Run Duke programme depending on the different commands
     * given by user.
     *
     * @throws IOException if file does not exist.
     */
    public void run() throws IOException {
        storage.handleLoad();
        System.out.println(ui.greeting());
        System.out.println(ui.showList());
        boolean isExit = false;
        while (!isExit) {
            try {
                Command c = new Command(taskList, ui);
                String toEcho = ui.getCommand();
                if (toEcho.equals("bye")) {
                    System.out.println(ui.bye());
                    isExit = true;
                } else if (toEcho.equals("list")) {
                    System.out.println(ui.showList());
                } else if (toEcho.startsWith("done")) {
                    System.out.println(c.handleDone(toEcho));
                } else if (toEcho.startsWith("todo")) {
                    System.out.println(c.handleTodo(toEcho));
                } else if (toEcho.startsWith("deadline")) {
                    System.out.println(c.handleDeadline(toEcho));
                } else if (toEcho.startsWith("event")) {
                    System.out.println(c.handleEvent(toEcho));
                } else if (toEcho.startsWith("delete")) {
                    System.out.println(c.handleDelete(toEcho));
                } else if (toEcho.startsWith("find")) {
                    System.out.println(c.handleFind(toEcho));
                } else {
                    throw new DukeUnknownCommandException();
                }
                storage.saveTasks();
            } catch (DateTimeParseException e) {
                ui.printDateTimeParseError(e.getMessage());
            } catch (DukeException e) {
                ui.printError(e.getMessage());
            }
        }
    }

    /**
     * You should have your own function to generate a response to user input.
     * Replace this stub with your completed method.
     */
    public String getResponse(String input) {
        try {
            Command c = new Command(taskList, ui);
            if (input.equals("bye")) {
                storage.saveTasks();
                return ui.bye();
            } else if (input.equals("list")) {
                storage.saveTasks();
                return ui.showList();
            } else if (input.startsWith("done")) {
                storage.saveTasks();
                return c.handleDone(input);
            } else if (input.startsWith("todo")) {
                storage.saveTasks();
                return c.handleTodo(input);
            } else if (input.startsWith("deadline")) {
                storage.saveTasks();
                return c.handleDeadline(input);
            } else if (input.startsWith("event")) {
                storage.saveTasks();
                return c.handleEvent(input);
            } else if (input.startsWith("delete")) {
                storage.saveTasks();
                return c.handleDelete(input);
            } else if (input.startsWith("find")) {
                storage.saveTasks();
                return c.handleFind(input);
            } else {
                throw new DukeUnknownCommandException();
            }
        } catch (DateTimeParseException e) {
            return e.getMessage();
        } catch (DukeException e) {
            return e.getMessage();
        }
    }

}
