import exceptions.DukeException;
import exceptions.DukeInvalidMessageException;
import exceptions.DukeUnknownCommandException;
import exceptions.DukeEmptyMessageException;

import java.io.*;
import java.time.format.DateTimeParseException;

public class Duke {

    private Ui ui;
    private Parser parser;
    private Storage storage;
    public final static String FILEPATH = System.getProperty("user.dir") + (System.getProperty("user.dir").endsWith("text-ui-test")
            ? "/saved-tasks.txt"
            : "/text-ui-test/saved-tasks.txt");

    public Duke() {
        ui = new Ui();
        parser = new Parser();
        storage = new Storage(FILEPATH);
    }

    public static void main(String[] args) throws IOException {
        new Duke().run();
    }

    public void run() throws IOException {

        storage.handleLoad();
        ui.greeting();
        ui.showList();
        boolean isExit = false;
        while (!isExit) {
            try {
                String toEcho = ui.getCommand();
                String[] command = parser.splitCommandAndDescription(toEcho);
                if (toEcho.equals("bye")) {
                    ui.bye();
                    isExit = true;
                } else if (toEcho.equals("list")) {
                    ui.showList();
                } else if (toEcho.startsWith("done")) {
                    if (toEcho.length() == 4) {
                        throw new DukeEmptyMessageException("Done");
                    } else if (Integer.parseInt(command[1]) > Task.tasks.size()) {
                        throw new DukeInvalidMessageException();
                    } else {
                        int index = Integer.parseInt(command[1]) - 1;
                        Task.tasks.get(index).markAsDone();
                        ui.printDone(index);
                    }
                } else if (toEcho.startsWith("todo")) {
                    if (toEcho.length() == 4) {
                        throw new DukeEmptyMessageException("Todo");
                    }
                    handleTodo(command[1]);
                } else if (toEcho.startsWith("deadline")) {
                    if (toEcho.length() == 8) {
                        throw new DukeEmptyMessageException("Deadline");
                    }
                    handleDeadline(command[1]);
                } else if (toEcho.startsWith("event")) {
                    if (toEcho.length() == 5) {
                        throw new DukeEmptyMessageException("Event");
                    }
                    handleEvent(command[1]);
                } else if (toEcho.startsWith("delete")) {
                    if (toEcho.length() == 6) {
                        throw new DukeEmptyMessageException("Delete");
                    } else if (Integer.parseInt(command[1]) > Task.tasks.size() ||
                            Integer.parseInt(command[1]) < 0) {
                        throw new DukeInvalidMessageException();
                    }
                    int indexToDelete = Integer.parseInt(command[1]) - 1;
                    ui.printDelete(indexToDelete);
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

    public void handleTodo(String description) {
        Todo todo = new Todo(description);
        Task.tasks.add(todo);
        ui.printTask(todo);
    }

    public void handleDeadline(String description) {
        String[] strArr = parser.splitDeadlineTime(description);
        String todo = strArr[0];
        String time = strArr[1];
        Deadline deadline = new Deadline(todo, time);
        Task.tasks.add(deadline);
        ui.printTask(deadline);
    }

    public void handleEvent(String description) {
        String[] strArr = parser.splitEventTime(description);
        String todo = strArr[0];
        String time = strArr[1];
        Event event = new Event(todo, time);
        Task.tasks.add(event);
        ui.printTask(event);
    }

}
