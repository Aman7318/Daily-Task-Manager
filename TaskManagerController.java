package com.example.task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextInputDialog;

import java.io.*;
import java.util.Optional;

public class TaskManagerController {
    @FXML
    private ListView<Task> taskListView;

    private ObservableList<Task> tasks;
    private final String filename = "tasks.txt";

    @FXML
    public void initialize() {
        tasks = FXCollections.observableArrayList();
        loadTasks();

        taskListView.setItems(tasks);
        taskListView.setCellFactory(param -> new TaskListCell());
    }

    @FXML
    private void showAddTaskDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Task");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter task title:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(title -> {
            Task newTask = new Task(title, "");
            tasks.add(newTask);
            saveTasks();
        });
    }

    @FXML
    private void markTaskAsCompleted() {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            selectedTask.setCompleted(true);
            saveTasks();
        }
    }

    @FXML
    private void deleteTask() {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            tasks.remove(selectedTask);
            saveTasks();
        }
    }

    public void saveTasks() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Task task : tasks) {
                writer.println(task.toDataString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTasks() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = Task.fromDataString(line);
                tasks.add(task);
            }
        } catch (IOException e) {
            // Handle file not found or other errors
        }
    }

    private static class TaskListCell extends ListCell<Task> {
        @Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);

            if (empty || task == null) {
                setText(null);
            } else {
                setText(task.toString());
                if (task.isCompleted()) {
                    setStyle("-fx-background-color: #90EE90;");  // Light Green background for completed tasks
                } else {
                    setStyle(null);
                }
            }
        }
    }
}
