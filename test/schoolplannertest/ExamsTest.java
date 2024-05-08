package schoolplannertest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import schoolplanner.examWindow.ExamsManager;
import schoolplanner.examWindow.Exams;

import java.util.ArrayList;

public class ExamsTest {
    @Test
    public void testAddExam() {
        // String name, int examTime, String subject, String priority, String notes
        ExamsManager manager = new ExamsManager();
        Exams exam1 = new Exams("Midterm 1", 1111111, "math 1101", "medium", "");
        Exams exam2 = new Exams("Final", 1111111, "ITSC 2214", "medium", "");
        Exams exam3 = new Exams("Quiz 4", 1111111, "STAT 2223", "medium", "");

        assertTrue(manager.getAllExams().isEmpty());

        manager.addExam(exam1);
        manager.addExam(exam2);
        manager.addExam(exam3);

        assertEquals(3, manager.getAllExams().size());
        assertTrue(manager.getAllExams().contains(exam1));
        assertTrue(manager.getAllExams().contains(exam2));
        assertTrue(manager.getAllExams().contains(exam3));

    }

    @Test
    public void testEditExam() {
        ExamsManager manager = new ExamsManager();
        Exams exam1 = new Exams("Midterm 1", 1111111, "math 1101", "medium", "");
        Exams exam2 = new Exams("Final", 1111111, "ITSC 2214", "medium", "");
        Exams exam3 = new Exams("Quiz 4", 1111111, "STAT 2223", "medium", "test");

        manager.addExam(exam1);
        manager.addExam(exam2);
        manager.addExam(exam3);

        manager.editExam(exam3, "Quiz 10", 100, "STAT 1220", "high", "");
        assertEquals("Quiz 10", exam3.getName());
        assertEquals(100, exam3.getExamTime());
        assertEquals("STAT 1220", exam3.getSubject());
        assertEquals("high", exam3.getPriority());
        assertEquals("test", exam3.getNotes());
    }

    @Test
    public void testRemoveExam() {
        ExamsManager manager = new ExamsManager();
        Exams exam1 = new Exams("Midterm 1", 1111111, "math 1101", "medium", "");
        Exams exam2 = new Exams("Final", 1111111, "ITSC 2214", "medium", "");
        Exams exam3 = new Exams("Quiz 4", 1111111, "STAT 2223", "medium", "test");

        manager.addExam(exam1);
        manager.addExam(exam2);
        manager.addExam(exam3);

        assertEquals(3, manager.getAllExams().size());

        try {
            manager.removeExam(exam3);
        } catch (Exception e) {
            fail("Unexpected error thrown");
        }

        assertEquals(2, manager.getAllExams().size());
        assertFalse(manager.getAllExams().contains(exam3));

    }

    @Test
    public void testGetAllExams() {
        ExamsManager manager = new ExamsManager();
        Exams exam1 = new Exams("Midterm 1", 1111111, "math 1101", "medium", "");
        Exams exam2 = new Exams("Final", 1111111, "ITSC 2214", "medium", "");
        Exams exam3 = new Exams("Quiz 4", 1111111, "STAT 2223", "medium", "test");

        manager.addExam(exam1);
        manager.addExam(exam2);
        manager.addExam(exam3);

        ArrayList<Exams> list = new ArrayList();
        list.add(exam1);
        list.add(exam2);
        list.add(exam3);

        assertEquals(list, manager.getAllExams());
    }

    @Test
    public void testSaveExamData() {
        ExamsManager manager = new ExamsManager();
        Exams exam1 = new Exams("Midterm 1", 1111111, "math 1101", "medium", "");
        Exams exam2 = new Exams("Final", 1111111, "ITSC 2214", "medium", "");
        Exams exam3 = new Exams("Quiz 4", 1111111, "STAT 2223", "medium", "test");

        manager.addExam(exam1);
        manager.addExam(exam2);
        manager.addExam(exam3);

        ExamsManager newManager = new ExamsManager();

        manager.saveExamData();
        newManager.loadExamData();

        assertEquals(exam1.getName(), newManager.getAllExams().get(0).getName());
        assertEquals(exam2.getName(), newManager.getAllExams().get(1).getName());
        assertEquals(exam3.getName(), newManager.getAllExams().get(2).getName());
    }
}
