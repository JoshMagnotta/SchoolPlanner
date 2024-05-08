package schoolplanner.csvWindow;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import schoolplanner.assignmentWindow.Assignments;
import schoolplanner.assignmentWindow.AssignmentsManager;
import schoolplanner.examWindow.Exams;
import schoolplanner.examWindow.ExamsManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Exports existing data to a CSV file
 */

public class ExportCSVController {

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleBrowseAndExport() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory to Save CSV Files");
        File selectedDirectory = directoryChooser.showDialog(dialogStage);

        if (selectedDirectory != null) {
            exportAssignments(selectedDirectory);
            exportExams(selectedDirectory);

            dialogStage.close();

            showAlert("Export Successful", "Data has been exported successfully!");

        }
    }

    /**
     * Saves the CSV to a specified directory based on user input
     * @param directory user selected directory
     */
    private void exportAssignments(File directory) {
        File file = new File(directory, "saveAssignmentData.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            List<Assignments> assignments = AssignmentsManager.getInstance().getAllAssignments();
            for (Assignments assignment : assignments) {
                writer.write(String.join(",",
                        assignment.getName(),
                        String.valueOf(assignment.getDueDate()),
                        assignment.getPriority(),
                        assignment.getSubject(),
                        assignment.getLink(),
                        assignment.getNotes(),
                        String.valueOf(assignment.isComplete())
                ));
                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the CSV to a specified directory based on user input
     * @param directory user selected directory
     */
    private void exportExams(File directory) {
        File file = new File(directory, "saveExamData.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            List<Exams> exams = ExamsManager.getInstance().getAllExams();
            for (Exams exam : exams) {
                writer.write(String.join(",",
                        exam.getName(),
                        String.valueOf(exam.getExamTime()),
                        exam.getSubject(),
                        exam.getPriority(),
                        exam.getNotes()
                ));
                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
