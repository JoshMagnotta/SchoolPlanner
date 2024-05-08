package schoolplanner.examWindow;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Controller for adding exams
 */
public class AddExamBoxController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField subjectField;
    @FXML
    private DatePicker examDatePicker;
    @FXML
    private ComboBox<String> priorityField;
    @FXML
    private TextArea notesField;

    private Stage dialogStage;
    private boolean okClicked = false;
    private ExamsManager manager;

    private Exams exam;

    @FXML
    private void initialize() {
        priorityField.getItems().addAll("High", "Medium", "Low");
        manager = ExamsManager.getInstance(); // Initialize or get singleton instance

    }

    public void setExam(Exams exam) {
        this.exam = exam;
        fillDialogFields();
    }

    /**
     * If editing an assignment, existing fields are loaded
     */
    private void fillDialogFields() {
        if (exam != null) {
            nameField.setText(exam.getName());
            examDatePicker.setValue(LocalDate.ofInstant(Instant.ofEpochSecond(exam.getExamTime()), ZoneId.systemDefault()));
            priorityField.setValue(exam.getPriority());
            subjectField.setText(exam.getSubject());
            notesField.setText(exam.getNotes());
        }
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            String name = nameField.getText();
            String subject = subjectField.getText();
            LocalDate examDate = examDatePicker.getValue();
            int examTime = (int) examDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
            String priority = priorityField.getSelectionModel().getSelectedItem();
            String notes = notesField.getText();

            Exams newExam = new Exams(name, examTime, subject, priority, notes);
            manager.addExam(newExam);

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    /**
     * Data validation
     * @return true if valid, false if invalid. Shows alert if invalid
     */
    private boolean isInputValid() {
        StringBuilder errors = new StringBuilder();
        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errors.append("No valid exam name!\n");
        }
        if (subjectField.getText() == null || subjectField.getText().length() == 0) {
            errors.append("No valid subject!\n");
        }
        if (examDatePicker.getValue() == null) {
            errors.append("No valid exam date!\n");
        }
        if (priorityField.getSelectionModel().isEmpty()) {
            errors.append("No priority selected!\n");
        }

        if (errors.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errors.toString());
            alert.showAndWait();
            return false;
        }
    }
}
