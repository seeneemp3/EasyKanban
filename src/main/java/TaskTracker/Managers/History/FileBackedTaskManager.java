package TaskTracker.Managers.History;

import TaskTracker.Managers.InMemoryTaskManager;
import TaskTracker.Managers.TaskManager;
import TaskTracker.Tasks.Epic;
import TaskTracker.Tasks.SubTask;
import TaskTracker.Tasks.Task;
import TaskTracker.Tasks.TaskType;
import TaskTracker.Utility.CSVConverter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    File filename;

    public FileBackedTaskManager(File filename) {
        this.filename = filename;
    }

    public FileBackedTaskManager() {
    }

    public void save() {
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(filename);
        try (BufferedWriter buffWriter = new BufferedWriter(new FileWriter(filename, StandardCharsets.UTF_8, false))) {
            buffWriter.write("id,type,name,status,description,localtime,duration,epic\n");
            for (Task t : tasks.values()) {
                buffWriter.write(t + "\n");
            }
            for (Task t : epics.values()) {
                buffWriter.write(t + "\n");
            }
            for (Task t : subtasks.values()) {
                buffWriter.write(t + "\n");
            }

            buffWriter.write("History " + historyManager);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileBackedTaskManager readFromFile() {
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(filename);
        List<String> lines = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            while (bufferedReader.ready()) {
                lines.add(bufferedReader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (lines.isEmpty()) return taskManager;

        for (int i = 1; i <= lines.size() - 2; i++) {
            String taskLine = lines.get(i);
            Task task = CSVConverter.toTask(taskLine);
            if (task.getType().equals(TaskType.TASK)) {
                taskManager.tasks.put(task.getId(), task);
            } else if (task.getType().equals(TaskType.EPIC)) {
                taskManager.epics.put(task.getId(), (Epic) task);
            } else if (task.getType().equals(TaskType.SUBTASK)) {
                taskManager.subtasks.put(task.getId(), (SubTask) task);
            }
        }
        if (!(lines.get(lines.size() - 1).substring(8).trim().equals(""))) {
            Arrays.stream(lines.get(lines.size() - 1).substring(8).split(",")).forEach(id -> {
                        int parsedId = Integer.parseInt(id);
                        if (tasks.containsKey(parsedId)) {
                            historyManager.addTask(tasks.get(parsedId));
                        } else if (epics.containsKey(parsedId)) {
                            historyManager.addTask(epics.get(parsedId));
                        } else if (subtasks.containsKey(parsedId)) {
                            historyManager.addTask(subtasks.get(parsedId));
                        }
                    }
            );
        }
        return taskManager;
    }

    @Override
    public Task getTask(int id) {
        Task t = super.getTask(id);
        save();
        return t;
    }

    @Override
    public List<Task> getAllTasks() {
        return super.getAllTasks();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public Epic getEpic(int id) {
        Epic e = super.getEpic(id);
        save();
        return e;
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateEpicStatus(int epicId) {
        super.updateEpicStatus(epicId);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask s = super.getSubTask(id);
        save();
        return s;
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void updateSubtask(SubTask subTask) {
        super.updateSubtask(subTask);
        save();
    }


    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

}

