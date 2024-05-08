package schoolplanner.assignmentWindow;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import schoolplanner.assignmentWindow.Assignments;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Controller for Assignment Details Popup
 */
public class AssignmentDetailsDialogController {

    @FXML
    private TextField nameField;
    @FXML private TextField dueDateField;
    @FXML private TextField priorityField;
    @FXML private TextField subjectField;
    @FXML private TextField urlField;
    @FXML private TextField notesField;

    /**
     * Gets fields from existing assignment to display
     * @param assignment Assignment the user selected
     */
    public void setAssignment(Assignments assignment) {
        nameField.setText("Name: " + assignment.getName());
        dueDateField.setText("Due Date: " + LocalDate.ofInstant(Instant.ofEpochSecond(assignment.getDueDate()), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("MMM dd yyyy")));
        priorityField.setText("Priority: " + assignment.getPriority());
        subjectField.setText("Subject: " + assignment.getSubject());
        urlField.setText("Link: " + assignment.getLink());
        notesField.setText("Notes: " + assignment.getNotes());
    }
}
