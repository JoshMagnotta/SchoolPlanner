package schoolplanner.assignmentWindow;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import schoolplanner.assignmentWindow.Assignments;
import schoolplanner.assignmentWindow.AssignmentsManager;

import java.time.ZoneId;
import java.time.LocalDate;
import java.time.Instant;

/**
 * Controller for Add Assignment Popup
 */
public class AddAssignmentBoxController {

    @FXML
    private TextField assignmentNameField;
    @FXML
    private DatePicker dueDateField;
    @FXML
    private TextField subjectField;
    @FXML
    private TextField linkField;
    @FXML
    private TextArea notesField;
    @FXML
    private ComboBox<String> priorityField;

    private AssignmentsManager manager;

    private Stage dialogStage;
    private boolean okClicked = false;

    private Assignments assignment; // Existing assignment for editing


    @FXML
    private void initialize() {
        priorityField.setItems(FXCollections.observableArrayList("Low", "Medium", "High"));
        manager = AssignmentsManager.getInstance();
    }

    public void setAssignment(Assignments assignment) {
        this.assignment = assignment;
        fillDialogFields();
    }

    /**
     * If editing an assignment, it pulls existing fields
     */
    private void fillDialogFields() {
        if (assignment != null) {
            assignmentNameField.setText(assignment.getName());
            dueDateField.setValue(LocalDate.ofInstant(Instant.ofEpochSecond(assignment.getDueDate()), ZoneId.systemDefault()));
            priorityField.setValue(assignment.getPriority());
            subjectField.setText(assignment.getSubject());
            linkField.setText(assignment.getLink());
            notesField.setText(assignment.getNotes());
        }
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            // Create a new assignment if null, otherwise update the existing one
            if (assignment == null) {
                assignment = new Assignments();
            }
            assignment.setName(assignmentNameField.getText());
            assignment.setDueDate((int) dueDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toEpochSecond());
            assignment.setPriority(priorityField.getSelectionModel().getSelectedItem());
            assignment.setSubject(subjectField.getText());
            assignment.setLink(linkField.getText());
            assignment.setNotes(notesField.getText());

            if (!manager.getAllAssignments().contains(assignment)) {
                manager.addAssignment(assignment);
            }

            dialogStage.close();
        }
    }

    /**
     * Data Validation
     * @return true if valid, false if invalid. Alert thrown if input is not valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (assignmentNameField.getText() == null || assignmentNameField.getText().length() == 0) {
            errorMessage += "No valid assignment name!\n";
        }
        if (dueDateField.getValue() == null) {
            errorMessage += "No valid due date!\n";
        }
        if (subjectField.getText() == null || subjectField.getText().length() == 0) {
            errorMessage += "No valid subject!\n";
        }
        if (priorityField.getSelectionModel().isEmpty()) {
            errorMessage += "No priority selected!\n";
        }
        // More checks can be added here as needed

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
