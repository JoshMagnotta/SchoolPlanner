module schoolplanner.schoolplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires com.jfoenix;
    requires com.calendarfx.view;


    opens schoolplanner.mainwindow to javafx.fxml;
    exports schoolplanner.mainwindow;
    exports schoolplanner.calendarWindow;
    opens schoolplanner.calendarWindow to javafx.fxml;
    exports schoolplanner.assignmentWindow;
    opens schoolplanner.assignmentWindow to javafx.fxml;
    exports schoolplanner.examWindow;
    opens schoolplanner.examWindow to javafx.fxml;
    exports schoolplanner.csvWindow;
    opens schoolplanner.csvWindow to javafx.fxml;
}