package com.example.task;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class Task implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String title;
    private final String description;
    private boolean completed;
    private Date dateAdded;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.completed = false;
        this.dateAdded = new Date();
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String toDataString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return title + ";" + description + ";" + completed + ";" + sdf.format(dateAdded);
    }

    public static Task fromDataString(String dataString) {
        String[] parts = dataString.split(";");
        if (parts.length == 4) {
            Task task = new Task(parts[0], parts[1]);
            task.setCompleted(Boolean.parseBoolean(parts[2]));
            try {
                task.dateAdded = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(parts[3]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return task;
        } else {
            // Handle invalid data format
            return null;
        }
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String status = completed ? "[Completed]" : "[Pending]";
        return status + " " + title + ": " + description + " (Added on " + sdf.format(dateAdded) + ")";
    }

    public static class TaskManagerFX extends Application {

        private ObservableList<Task> tasks;
        private final String filename = "tasks.txt";

        public static void main(String[] args) {
            launch(args);
        }

        @Override
        public void start(Stage primaryStage) throws IOException, IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TaskManager.fxml"));
            Parent root = loader.load();

            primaryStage.setTitle("Task Manager");
            primaryStage.setScene(new Scene(root, 400, 400));
            primaryStage.show();

            TaskManagerController controller = loader.getController();
            controller.initialize(); // Call initialize explicitly to load tasks

            // Save tasks on application exit
            primaryStage.setOnCloseRequest(event -> controller.saveTasks());
        }
    }

}
