package duke;

import duke.command.Command;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
* Storage class to store tasks into the TaskList and load tasks from external files.
*
* @author Sheryl Kong (A0240686Y)
*/

public class Storage {
    private final String filePath;
    private static boolean isLoadingFile = false;

    /**
     * Constructor for Storage class
     */

    public Storage(String filePath) {
        this.filePath = filePath;
        try {
            File file = new File(filePath);
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the program is loading from the file
     *
     * @return boolean
     */

    public boolean checkIsLoadingFile() {
        return isLoadingFile;
    }

    public String read() throws IOException {
        return Files.readString(Path.of(this.filePath));
    }

    /**
     * Loads the content from the file into a TaskList.
     *
     * @return TaskList
     * @throws DukeException when input is invalid
     */

    public TaskList load() throws DukeException {
        isLoadingFile = true;
        TaskList taskList = new TaskList();
        try {
            String fileContent = this.read();
            String[] lines = fileContent.split(System.lineSeparator());
            for (String line : lines) {
                Command command = Parser.parseFileLine(line);
                command.execute(taskList, this);
            }
        } catch (FileNotFoundException f) {
            throw new DukeException("File not found");
        } catch (IOException i) {
            throw new DukeException("IO exception");
        }
        isLoadingFile = false;
        return taskList;
    }

    /**
     * Saves the tasks in the TaskList into the file.
     *
     * @param taskList TaskList
     * @throws DukeException when input is invalid
     */

    public void saveData(TaskList taskList) throws DukeException {
        try {
            File f = new File(this.filePath);
            if (!f.createNewFile()) f.delete(); // if file exists, delete it
            f.createNewFile(); // create a new file
            FileWriter fw = new FileWriter(this.filePath, true); //create a new FileWriter object to the file
            for (Task t : taskList.getList()) {
                fw.write(t.toString() + System.lineSeparator());
            } //for every task in the task list, write it to the empty FileWriter object.
            fw.close(); // close FileWriter
        } catch (IOException i) {
            throw new DukeException("OOPS!! File does not exist");
        }
    }


}

