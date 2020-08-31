package duke;

import java.util.ArrayList;
import java.util.List;

import duke.task.Task;

/**
 * Handles actions by user with regards to all their tasks.
 */
public class TaskList {
    private Storage savedStorage;
    private List<Task> allItems;

    /**
     * Creates an empty TaskList.
     */
    public TaskList() {
        this.savedStorage = new Storage();
        this.allItems = this.savedStorage.getSavedTasks();
    }

    /**
     * Adds task to the current list of tasks.
     *
     * @param toAdd Task to add
     */
    public void addTask(Task toAdd) {
        this.allItems.add(toAdd);
        String printTask = "Alright, its in your list now!\n\t" + toAdd
                + "\nNow you have " + this.allItems.size() + " tasks.\n" + Ui.LINE;
        Ui.printMessageToUser(printTask);
    }

    /**
     * Retrieves all the tasks currently stored.
     *
     * @return the tasks currently stored in a List
     */
    public List<Task> getAllTasks() {
        return this.allItems;
    }

    /**
     * Prints the output of all tasks in store currently.
     */
    public void printStore() {
        String printList;
        if (this.allItems.size() == 0) {
            printList = "There are no tasks added till now.\nAdd one by just typing its name.\n"
                    + Ui.LINE;
        } else {
            printList = "Please finish these tasks ASAP!\n";
            int counter = 1;
            for (Task task : this.allItems) {
                printList = printList.concat("[" + counter + "] " + task + "\n");
                counter++;
            }
            printList = printList.concat("If you're brave enough to start,\n"
                    + "You're strong enough to finish it!\n" + Ui.LINE);
        }
        Ui.printMessageToUser(printList);
    }

    /**
     * Sets the selected task to be completed.
     *
     * @param index index of task to be set completed.
     */
    public void completeTask(int index) {
        Task toComplete = this.allItems.get(index);
        toComplete.finishTask();
    }

    /**
     * Removes task from current list.
     *
     * @param index index of task to be removed.
     */
    public void deleteTask(int index) {
        this.allItems.remove(index);
        Ui.printDeleteTaskMessage();
    }

    /**
     * Sends instruction to save the current tasks.
     */
    public void saveIntoHarddisk() {
        this.savedStorage.saveIntoHarddisk();
    }

    /**
     * Finds and prints matching tasks.
     * @param toMatch the word to match with.
     */
    public void matchTasks(String toMatch) {
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : this.allItems) {
            if (task.canMatch(toMatch)) {
                matchingTasks.add(task);
            }
        }

        String printMatchingTasks;
        if (matchingTasks.size() == 0) {
            printMatchingTasks = "There are no tasks that match " + toMatch + "\n" + Ui.LINE;
        } else {
            printMatchingTasks = "Matching tasks: \n";
            for (Task task : matchingTasks) {
                printMatchingTasks = printMatchingTasks.concat(task + "\n");
            }
            printMatchingTasks = printMatchingTasks + Ui.LINE;
        }
        System.out.println(printMatchingTasks);
    }
}
