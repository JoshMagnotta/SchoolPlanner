package schoolplanner.csvWindow;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import schoolplanner.assignmentWindow.Assignments;
import schoolplanner.assignmentWindow.AssignmentsManager;
import schoolplanner.examWindow.Exams;
import schoolplanner.examWindow.ExamsManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Import CSV to application
 */
public class ImportCSVController {

    @FXML
    private TextField assignmentFilePathField;
    @FXML
    private TextField examFilePathField;

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Select the CSV file
     */
    @FXML
    private void handleAssignmentBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        File selectedFile = fileChooser.showOpenDialog(dialogStage);
        if (selectedFile != null) {
            assignmentFilePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void handleExamBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        File selectedFile = fileChooser.showOpenDialog(dialogStage);
        if (selectedFile != null) {
            examFilePathField.setText(selectedFile.getAbsolutePath());
        }
    }


    /**
     * Parse through the CSV and add to application
     */
    private void assignmentImport() {
        AssignmentsManager assignmentsManager = AssignmentsManager.getInstance();

        try {
            BufferedReader brAssignment = new BufferedReader(new FileReader(assignmentFilePathField.getText()));
            String line;
            while ((line = brAssignment.readLine()) != null) {
                String[] values = line.split(",");

                assignmentsManager.addAssignment(new Assignments(values[0], Integer.parseInt(values[1]), values[2], values[3], values[4], values[5], Boolean.parseBoolean(values[6])));
            }
        } catch (Exception e) {
            System.out.println("An error occurred importing the assignment" + e.getMessage());
        }
    }

    private void examImport() {
        ExamsManager examsManager = ExamsManager.getInstance();
        try {
            BufferedReader brExam = new BufferedReader(new FileReader(examFilePathField.getText()));
            String lineExam;
            while ((lineExam = brExam.readLine()) != null) {
                String[] values = lineExam.split(",");

                examsManager.addExam(new Exams(values[0], Integer.parseInt(values[1]), values[2], values[3], values[4]));
            }

            brExam.close();

        } catch(Exception e) {
            System.out.println("An error occurred importing the exams" + e.getMessage());
        }
    }

    @FXML
    private void handleImport() {
        assignmentImport();
        examImport();
        dialogStage.close();

    }
}

