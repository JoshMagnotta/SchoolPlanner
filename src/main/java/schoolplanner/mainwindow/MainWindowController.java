package schoolplanner.mainwindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import schoolplanner.assignmentWindow.AddAssignmentBoxController;
import schoolplanner.assignmentWindow.AssignmentDetailsDialogController;
import schoolplanner.assignmentWindow.Assignments;
import schoolplanner.assignmentWindow.AssignmentsManager;
import schoolplanner.csvWindow.ImportCSVController;
import schoolplanner.csvWindow.ExportCSVController;
import schoolplanner.examWindow.AddExamBoxController;
import schoolplanner.examWindow.ExamDetailsDialog;
import schoolplanner.examWindow.Exams;
import schoolplanner.examWindow.ExamsManager;
import javafx.scene.control.ListView;


import java.io.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

/**
 * Controller for the main window
 */
public class MainWindowController {

    @FXML
    private ListView<Assignments> assignmentsList;

    @FXML
    private ListView<Exams> examsList;

    @FXML
    private VBox rootPane;

    @FXML
    private Button sortButton;

    @FXML
    private Button themeToggleButton;

    private boolean sortByDate = true;

    private boolean darkMode = false;


    private AssignmentsManager assignmentsManager = AssignmentsManager.getInstance();

    private ExamsManager examsManager = ExamsManager.getInstance();

    public MainWindowController() {
        assignmentsList = new ListView<>();
        examsList = new ListView<>();
    }

    @FXML
    public void initialize() {
        rootPane.getStylesheets().add(getClass().getResource("/css/light-mode.css").toExternalForm());
        themeToggleButton.setText("Dark Mode");

        loadLists();
        configureListView();
        configureExamListView();

        // Update Assignment List View
        assignmentsList.setCellFactory(param -> new ListCell<Assignments>() {
            @Override
            protected void updateItem(Assignments assignment, boolean empty) {
                super.updateItem(assignment, empty);
                getStyleClass().removeAll("assignment-due-soon", "assignment-overdue", "completed"); // Remove styles to prevent duplication
                if (empty || assignment == null) {
                    setText(null);
                    setGraphic(null);
                    setContextMenu(null);
                } else {
                    LocalDate dueDate = Instant.ofEpochSecond(assignment.getDueDate()).atZone(ZoneId.systemDefault()).toLocalDate();
                    String formattedDate = dueDate.format(DateTimeFormatter.ofPattern("MMM dd"));
                    setText(assignment.getName() + ", " + assignment.getSubject() + " - Due: " + formattedDate);

                    if (assignment.isComplete()) {
                        getStyleClass().add("completed");
                    } else {
                        LocalDate now = LocalDate.now();
                        if (dueDate.isBefore(now) && !assignment.isComplete()) {
                            getStyleClass().add("assignment-overdue");
                        } else if ((dueDate.isEqual(now) || dueDate.isEqual(now.plusDays(1))) && !assignment.isComplete()) {
                            getStyleClass().add("assignment-due-soon");
                        }
                    }

                    // Options on right click
                    ContextMenu contextMenu = new ContextMenu();
                    MenuItem editItem = new MenuItem("Edit");
                    MenuItem deleteItem = new MenuItem("Delete");
                    MenuItem completeItem = new MenuItem(assignment.isComplete() ? "Mark Incomplete" : "Mark as Done");

                    editItem.setOnAction(event -> {
                        handleEditAssignment(assignment);
                        assignmentsManager.saveData();

                    });
                    deleteItem.setOnAction(event -> {
                        handleDeleteAssignment(assignment);
                        assignmentsManager.saveData();
                    });
                    completeItem.setOnAction(event -> {
                        assignment.changeComplete();  // Assume toggleComplete() method flips the completion status
                        updateItem(assignment, false); // Refresh cell display after state change

                        assignmentsManager.saveData();
                    });

                    contextMenu.getItems().addAll(editItem, deleteItem, completeItem);
                    setContextMenu(contextMenu);
                }
            }
        });


        // Open Assignment/Exam details
        assignmentsList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !event.isSecondaryButtonDown()) {
                Assignments selectedAssignment = assignmentsList.getSelectionModel().getSelectedItem();
                if (selectedAssignment != null) {
                    showDetailsDialog(selectedAssignment);
                }
            }
        });

        examsList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !event.isSecondaryButtonDown()) {
                Exams selectedExam = examsList.getSelectionModel().getSelectedItem();
                if (selectedExam != null) {
                    showExamDetailsDialog(selectedExam);
                }
            }
        });
    }


    /**
     * Opens Assignment Details
     * @param assignment takes in assignment user selects
     */
    private void showDetailsDialog(Assignments assignment) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/schoolplanner.calendarWindow/assignment-details-dialog.fxml"));
            System.out.println(loader);
            Parent root = loader.load();
            AssignmentDetailsDialogController controller = loader.getController();
            controller.setAssignment(assignment);

            Stage stage = new Stage();
            stage.setTitle("Assignment Details");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens Exam Details
     * @param exam takes in exam user selects
     */
    private void showExamDetailsDialog(Exams exam) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/schoolplanner.examWindow/exam-details-dialog.fxml"));
            System.out.println(loader);
            Parent root = loader.load();
            ExamDetailsDialog controller = loader.getController();
            controller.setExam(exam);

            Stage stage = new Stage();
            stage.setTitle("Assignment Details");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Formats assignments list view
     */
    private void configureListView() {
        assignmentsList.setCellFactory(param -> new ListCell<Assignments>() {
            @Override
            protected void updateItem(Assignments item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    LocalDate dueDate = LocalDate.ofInstant(Instant.ofEpochSecond(item.getDueDate()), ZoneId.systemDefault());
                    setText(item.getName() + " - Due: " + dueDate.format(DateTimeFormatter.ofPattern("MMM dd")));

                }
            }
        });
    }

    /**
     * Formats exams list view
     */
    private void configureExamListView() {
        examsList.setCellFactory(param -> new ListCell<Exams>() {
            @Override
            protected void updateItem(Exams exam, boolean empty) {
                super.updateItem(exam, empty);
                if (empty || exam == null) {
                    setText(null);
                    setContextMenu(null);
                } else {
                    setText(exam.getName() + ", " + exam.getSubject() + " - Date: " + Instant.ofEpochSecond(exam.getExamTime())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                            .format(DateTimeFormatter.ofPattern("MMM dd")));
                    ContextMenu contextMenu = new ContextMenu();

                    MenuItem editItem = new MenuItem("Edit");
                    editItem.setOnAction(event -> handleEditExam(exam));

                    MenuItem deleteItem = new MenuItem("Delete");
                    deleteItem.setOnAction(event -> handleDeleteExam(exam));

                    contextMenu.getItems().addAll(editItem, deleteItem);
                    setContextMenu(contextMenu);
                }
            }
        });
    }

    @FXML
    private void handleToggleSort() {
        if (sortByDate) {
            sortButton.setText("Sort by Priority Score");
            sortByDate = false;
            sortAssignmentsByPriority();
        } else {
            sortButton.setText("Sort by Due Date");
            sortByDate = true;
            sortAssignmentsByDueDate();
        }
    }

    private void sortExamsByDueDate() {
        ObservableList<Exams> sortedExams = FXCollections.observableArrayList(examsManager.getAllExams());
        sortedExams.sort(Comparator.comparingInt(Exams::getExamTime));
        examsList.setItems(sortedExams);
        examsList.refresh();
    }

    private void sortAssignmentsByDueDate() {
        ObservableList<Assignments> sortedAssignments = FXCollections.observableArrayList(assignmentsManager.getAllAssignments());
        sortedAssignments.sort(Comparator.comparingInt(Assignments::getDueDate));
        assignmentsList.setItems(sortedAssignments);
        assignmentsList.refresh();
    }

    private void sortAssignmentsByPriority() {
        try {
            assignmentsManager.generatePriority(); // Ensure this method updates priority score correctly
        } catch (Exception e) {
            System.out.println(e + "No elements in the list to generate a priority for.");
        }
        ObservableList<Assignments> sortedAssignments = FXCollections.observableArrayList(assignmentsManager.getAllAssignments());
        sortedAssignments.sort(Comparator.comparingDouble(Assignments::getPriorityScore).reversed());
        assignmentsList.setItems(sortedAssignments);
        assignmentsList.refresh();
    }


    /**
     * Refresh lists when an assignment/exam updates
     */
    public void loadLists() {
        ObservableList<Assignments> assignments = FXCollections.observableArrayList(assignmentsManager.getAllAssignments());

        assignmentsList.setItems(assignments);

        ObservableList<Exams> examsNames = FXCollections.observableArrayList();

        examsList.setItems(examsNames);

        sortAssignmentsByPriority();
        sortExamsByDueDate();
    }

    @FXML
    private void handleAddAssignment() {
        showAddAssignmentDialog();
    }

    /**
     * Popup to add an assignment
     */
    private void showAddAssignmentDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/schoolplanner.assignmentWindow/add-assignment.fxml"));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Assignment");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(assignmentsList.getScene().getWindow());
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            AddAssignmentBoxController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

        }  catch (IOException e) {
            System.out.println("An error occurred! Invalid input entered");
        }

        loadLists();

    }

    public void handleAddExam(ActionEvent actionEvent) {
        showAddExamDialog();
    }

    /**
     * Popup to add an exam
     */
    private void showAddExamDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/schoolplanner.examWindow/add-exam-dialog.fxml"));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Exam");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(examsList.getScene().getWindow());
            dialogStage.setScene(new Scene(root));

            AddExamBoxController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();
        } catch (IOException e) {
            System.out.println("An error occurred! Invalid data entered");
        }

        loadLists();

    }

    private void handleEditAssignment(Assignments assignment) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/schoolplanner.assignmentWindow/add-assignment.fxml"));
            Parent root = loader.load();

            AddAssignmentBoxController controller = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Assignment");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(assignmentsList.getScene().getWindow());
            dialogStage.setScene(new Scene(root));

            controller.setDialogStage(dialogStage);
            controller.setAssignment(assignment);

            dialogStage.showAndWait();

            loadLists();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteAssignment(Assignments assignment) {
        try {
            assignmentsManager.removeAssignment(assignment);
            loadLists(); // Refresh list after deletion
        } catch (Exception e) {
            System.out.println("Couldn't find assignment");
        }

        loadLists();
    }

    private void handleMarkAsDone(Assignments assignment) {
        try {
            assignmentsManager.setComplete(assignment);

        } catch (Exception e) {
            System.out.println("Couldn't mark assignment as done");
        }
    }

    private void handleEditExam(Exams exam) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/schoolplanner.examWindow/add-exam-dialog.fxml"));
            Parent root = loader.load();

            AddExamBoxController controller = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Assignment");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(assignmentsList.getScene().getWindow());
            dialogStage.setScene(new Scene(root));

            examsManager.removeExam(exam);
            controller.setDialogStage(dialogStage);
            controller.setExam(exam);

            dialogStage.showAndWait();

            loadLists();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleDeleteExam(Exams exam) {
        try {
            examsManager.removeExam(exam);
            loadLists();
        } catch (Exception e) {
            System.out.println("Could not find exam: ");
        }
    }

    /**
     * Opens calendar view
     */
    @FXML
    private void handleOpenCalendar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/schoolplanner.calendarWindow/calendar-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Calendar View");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens import window
     */
    @FXML
    private void handleImportCSV() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/schoolplanner.csvWindow/importCSV.fxml"));
            VBox root = loader.load();
            ImportCSVController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Import CSV");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            controller.setDialogStage(stage);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadLists();
    }


    /**
     * Opens export window
     */
    @FXML
    private void handleExportCSV() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/schoolplanner.csvWindow/exportCSV.fxml"));
            VBox root = loader.load();
            ExportCSVController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Export CSV");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            controller.setDialogStage(stage);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadLists();
    }

    /**
     * Toggle between light and dark mode
     */
    @FXML
    private void toggleTheme() {
        if (!darkMode) {
            rootPane.getStylesheets().clear();
            rootPane.getStylesheets().add(getClass().getResource("/css/dark-mode.css").toExternalForm());
            darkMode = true;
            themeToggleButton.setText("Light Mode");
        } else {
            rootPane.getStylesheets().clear();
            rootPane.getStylesheets().add(getClass().getResource("/css/light-mode.css").toExternalForm());
            darkMode = false;
            themeToggleButton.setText("Dark Mode");
        }
    }
}
