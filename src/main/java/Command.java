import exceptions.DukeEmptyMessageException;
import exceptions.DukeInvalidMessageException;
import exceptions.DukeMissingTimeException;

public class Command {

    private TaskList taskList;
    private Ui ui;

    /**
     * Constructor for Command.
     *
     * @param taskList taskList
     * @param ui Ui
     */
    public Command(TaskList taskList, Ui ui) {
        this.taskList = taskList;
        this.ui = ui;
    }

    /**
     * Marks an item in the list as done and prints the necessary string message.
     *
     * @param toEcho command and description number.
     * @throws DukeEmptyMessageException thrown when description is empty.
     * @throws DukeInvalidMessageException thrown when description is not a valid number.
     */
    public void handleDone(String toEcho) throws DukeEmptyMessageException, DukeInvalidMessageException {
        String[] command = Parser.splitCommandAndDescription(toEcho);
        if (toEcho.length() == 4) {
            throw new DukeEmptyMessageException("Done");
        } else if (Integer.parseInt(command[1]) > TaskList.getTaskLists().size()) {
            throw new DukeInvalidMessageException();
        } else {
            int index = Integer.parseInt(command[1]) - 1;
            taskList.markDone(index);
            ui.printDone(index);
        }
    }

    /**
     * Adds todo into the list and print the relevant todo message.
     *
     * @param description description of todo.
     */
    public void handleTodo(String description) throws DukeEmptyMessageException {
        String[] command = Parser.splitCommandAndDescription(description);
        if (description.length() == 4) {
            throw new DukeEmptyMessageException("Todo");
        }
        Todo todo = new Todo(command[1]);
        TaskList.getTaskLists().add(todo);
        ui.printTask(todo);
    }

    /**
     * Adds deadline into the list and print the relevant deadline message.
     *
     * @param toEcho full description of deadline, including the command.
     * @throws DukeMissingTimeException thrown when user does not input the timing required.
     */
    public void handleDeadline(String toEcho) throws DukeMissingTimeException {
        try {
            String[] command = Parser.splitCommandAndDescription(toEcho);
            if (toEcho.length() == 8) {
                throw new DukeEmptyMessageException("Deadline");
            }
            String[] strArr = Parser.splitDeadlineTime(command[1]);
            String todo = strArr[0];
            String time = strArr[1];
            Deadline deadline = new Deadline(todo, time);
            taskList.add(deadline);
            ui.printTask(deadline);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DukeMissingTimeException();
        } catch (DukeEmptyMessageException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Adds event into the list and print the relevant event message.
     *
     * @param toEcho full description of event, including command.
     * @throws DukeMissingTimeException thrown when user does not input the timing required.
     */
    public void handleEvent(String toEcho) throws DukeMissingTimeException {
        try {
            String[] command = Parser.splitCommandAndDescription(toEcho);
            if (toEcho.length() == 5) {
                throw new DukeEmptyMessageException("Event");
            }
            String[] strArr = Parser.splitEventTime(command[1]);
            String todo = strArr[0];
            String time = strArr[1];
            Event event = new Event(todo, time);
            taskList.add(event);
            ui.printTask(event);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DukeMissingTimeException();
        } catch (DukeEmptyMessageException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Deletes an item from the list.
     *
     * @param toEcho delete command and description of type number.
     * @throws DukeEmptyMessageException thrown when description is empty.
     * @throws DukeInvalidMessageException thrown when description is not a valid number.
     */
    public void handleDelete(String toEcho) throws DukeEmptyMessageException, DukeInvalidMessageException {
        String[] command = Parser.splitCommandAndDescription(toEcho);
        if (toEcho.length() == 6) {
            throw new DukeEmptyMessageException("Delete");
        } else if (Integer.parseInt(command[1]) > TaskList.getTaskLists().size()
                || Integer.parseInt(command[1]) < 0) {
            throw new DukeInvalidMessageException();
        }
        int indexToDelete = Integer.parseInt(command[1]) - 1;
        ui.printDelete(indexToDelete);
    }

    /**
     * Finds the keyword passed in argument, and returns the items from list which
     * matches the keyword.
     *
     * @param toEcho command and description.
     * @throws DukeEmptyMessageException thrown when keyword is empty
     */
    public void handleFind(String toEcho) throws DukeEmptyMessageException {
        String[] command = Parser.splitCommandAndDescription(toEcho);
        if (toEcho.length() == 4) {
            throw new DukeEmptyMessageException("Find");
        }
        ui.printFind(command[1]);
    }
}
