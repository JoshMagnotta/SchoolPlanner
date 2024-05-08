package schoolplanner.examWindow;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Shows details when an exam is selected
 */
public class ExamDetailsDialog {
    @FXML
    private TextField nameField;
    @FXML private TextField dueDateField;
    @FXML private TextField priorityField;
    @FXML private TextField subjectField;
    @FXML private TextField notesField;

    public void setExam(Exams exam) {
        nameField.setText("Name: " + exam.getName());
        dueDateField.setText("Due Date: " + LocalDate.ofInstant(Instant.ofEpochSecond(exam.getExamTime()), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("MMM dd yyyy")));
        priorityField.setText("Priority: " + exam.getPriority());
        subjectField.setText("Subject: " + exam.getSubject());
        notesField.setText("Notes: " + exam.getNotes());
    }
}
