package shodan;

import shodan.command.Command;
import shodan.command.CommandParser;
import shodan.storage.StorageManager;
import shodan.tasks.Task;
import shodan.tasks.impl.Deadline;
import shodan.tasks.impl.Event;
import shodan.tasks.impl.Todo;
import shodan.ui.TermUi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;

/**
 * Shodan is a chatbot that is able to manage tasks.
 */
public class Shodan {
    private TaskList tasks;
    private static StorageManager storageManager;
    private TermUi ui;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        new Shodan().run();
    }

    /**
     * Instantiates a new Shodan instance.
     */
    public Shodan() {
        storageManager = new StorageManager();
        tasks = new TaskList(storageManager.loadTasks());
        ui = new TermUi();
    }

    /**
     * Starts the chatbot. Shodan will process each command until the 'bye'
     * command is entered.
     */
    public void run() {
        ui.showGreeting();
        Scanner sc = new Scanner(System.in);
        boolean shouldExit = false;
        while (!shouldExit) {
            try {
                ui.showPrompt();
                Command command = CommandParser.parse(sc.nextLine());
                shouldExit = command.execute(tasks, storageManager, ui);
            } catch (ShodanException e) {
                ui.printError(e);

            } catch (IllegalArgumentException e) {
                /* If no input was entered, re-prompt without showing any error */
            }
        }
    }
}
