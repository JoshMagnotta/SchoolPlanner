package schoolplanner.calendarWindow;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import javafx.fxml.FXML;
import schoolplanner.assignmentWindow.Assignments;
import schoolplanner.assignmentWindow.AssignmentsManager;
import schoolplanner.examWindow.Exams;
import schoolplanner.examWindow.ExamsManager;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * Controller for the popup Calendar
 */
public class CalendarController {

    @FXML
    private CalendarView calendarView;
    private Calendar calendar;

    @FXML
    public void initialize() {
        configureCalendar();
        calendar = new Calendar();
        calendar.setReadOnly(true);
    }

    private void configureCalendar() {
        calendarView.setRequestedTime(LocalTime.now());
        loadCalendarEntries();
    }

    /**
     * Loads existing entries that were entered on the Dashboard or imported via a CSV
     */
    private void loadCalendarEntries() {

        Calendar assignmentsCalendar = new Calendar("Assignments");
        Calendar examsCalendar = new Calendar("Exams");

        for (Assignments assignment : AssignmentsManager.getInstance().getAllAssignments()) {
            Entry<String> entry = new Entry<>(assignment.getName());
            entry.changeStartDate(LocalDate.ofInstant(Instant.ofEpochSecond(assignment.getDueDate()), ZoneId.systemDefault()));
            entry.setFullDay(true);
            assignmentsCalendar.addEntry(entry);
        }

        for (Exams exam : ExamsManager.getInstance().getAllExams()) {
            Entry<String> entry = new Entry<>(exam.getName());
            entry.changeStartDate(LocalDate.ofInstant(Instant.ofEpochSecond(exam.getExamTime()), ZoneId.systemDefault()));
            entry.setFullDay(true);
            examsCalendar.addEntry(entry);
        }

        CalendarSource myCalendarSource = new CalendarSource("My Calendars");
        myCalendarSource.getCalendars().clear();
        myCalendarSource.getCalendars().addAll(assignmentsCalendar, examsCalendar);

        calendarView.getCalendarSources().add(myCalendarSource);
    }
}
