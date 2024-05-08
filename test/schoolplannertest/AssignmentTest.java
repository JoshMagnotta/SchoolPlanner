package schoolplannertest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import schoolplanner.assignmentWindow.Assignments;
import schoolplanner.assignmentWindow.AssignmentsManager;

import java.util.ArrayList;

public class AssignmentTest {

    @Test
    public void testAddAssignment() {
        AssignmentsManager manager = new AssignmentsManager();

        // String name, int dueDate, String priority, String subject, String link, String notes
        Assignments assignment1 = new Assignments("Homework 1", 1709840522, "medium", "MATH 1101", "", "");
        Assignments assignment2 = new Assignments("Homework 2", 1709923095, "high", "MATH 1101", "", "");
        Assignments assignment3 = new Assignments("Homework 3", 1710009495, "low", "MATH 1101", "", "");
        Assignments assignment4 = new Assignments("Homework 4", 1710095895, "medium", "MATH 1101", "", "");

        assertTrue(manager.getAllAssignments().isEmpty());

        manager.addAssignment(assignment1);
        manager.addAssignment(assignment2);
        manager.addAssignment(assignment3);

        assertEquals(3, manager.getAllAssignments().size());

        assertEquals(assignment1, manager.getAllAssignments().get(0));
        assertEquals(assignment2, manager.getAllAssignments().get(1));
        assertEquals(assignment3, manager.getAllAssignments().get(2));

        assertFalse(manager.getAllAssignments().contains(assignment4));

    }

    @Test
    public void testEditAssignment() {
        AssignmentsManager manager = new AssignmentsManager();

        // String name, int dueDate, String priority, String subject, String link, String notes
        Assignments assignment1 = new Assignments("Homework 1", 1709840522, "medium", "MATH 1101", "", "");
        Assignments assignment2 = new Assignments("Homework 2", 1709923095, "high", "MATH 1101", "", "");
        Assignments assignment3 = new Assignments("Homework 3", 1710009495, "low", "MATH 1101", "", "");
        Assignments assignment4 = new Assignments("Homework 4", 1710095895, "medium", "MATH 1101", "", "");

        manager.addAssignment(assignment1);
        manager.addAssignment(assignment2);
        manager.addAssignment(assignment3);

        manager.editAssignment(assignment1, "Hw1", 0, "", "", "", "Test notes");
        manager.editAssignment(assignment4, "", 0, "", "", "", "");

        assertTrue(manager.getAllAssignments().contains(assignment4));
        assertEquals("Hw1", manager.getAllAssignments().get(0).getName());
    }

    @Test
    public void testRemoveAssignment() {
        AssignmentsManager manager = new AssignmentsManager();

        // String name, int dueDate, String priority, String subject, String link, String notes
        Assignments assignment1 = new Assignments("Homework 1", 1709840522, "medium", "MATH 1101", "", "");
        Assignments assignment2 = new Assignments("Homework 2", 1709923095, "high", "MATH 1101", "", "");
        Assignments assignment3 = new Assignments("Homework 3", 1710009495, "low", "MATH 1101", "", "");
        Assignments assignment4 = new Assignments("Homework 4", 1710095895, "medium", "MATH 1101", "", "");

        assertTrue(manager.getAllAssignments().isEmpty());

        manager.addAssignment(assignment1);
        manager.addAssignment(assignment2);
        manager.addAssignment(assignment3);

        try {
            manager.removeAssignment(assignment1);

            assertEquals(2, manager.getAllAssignments().size());
            assertFalse(manager.getAllAssignments().contains(assignment1));
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }

        try {
            manager.removeAssignment(assignment4);
            fail("Error not thrown correctly");
        } catch (Exception e) {
            assertTrue(true); // Exception thrown correctly
        }
    }

    @Test
    public void testChangeComplete() {
        AssignmentsManager manager = new AssignmentsManager();

        // String name, int dueDate, String priority, String subject, String link, String notes
        Assignments assignment1 = new Assignments("Homework 1", 1709840522, "medium", "MATH 1101", "", "");
        Assignments assignment2 = new Assignments("Homework 2", 1709923095, "high", "MATH 1101", "", "");
        Assignments assignment3 = new Assignments("Homework 3", 1710009495, "low", "MATH 1101", "", "");
        Assignments assignment4 = new Assignments("Homework 4", 1710095895, "medium", "MATH 1101", "", "");

        manager.addAssignment(assignment1);
        manager.addAssignment(assignment2);
        manager.addAssignment(assignment3);

        assertEquals(false, manager.getAllAssignments().get(0).isComplete());
        manager.getAllAssignments().get(0).changeComplete();
        assertEquals(true, manager.getAllAssignments().get(0).isComplete());

    }

    @Test
    public void testGetAllAssignments() {
        AssignmentsManager manager = new AssignmentsManager();

        Assignments assignment1 = new Assignments("Homework 1", 1709840522, "medium", "MATH 1101", "", "");
        Assignments assignment2 = new Assignments("Homework 2", 1709923095, "high", "MATH 1101", "", "");
        Assignments assignment3 = new Assignments("Homework 3", 1710009495, "low", "MATH 1101", "", "");
        Assignments assignment4 = new Assignments("Homework 4", 1710095895, "medium", "MATH 1101", "", "");

        manager.addAssignment(assignment1);
        manager.addAssignment(assignment2);
        manager.addAssignment(assignment3);

        ArrayList<Assignments> test = new ArrayList<>();
        test.add(assignment1);
        test.add(assignment2);
        test.add(assignment3);

        assertEquals(test, manager.getAllAssignments());
    }

    @Test
    public void testGetPriorityList() {
        AssignmentsManager manager = new AssignmentsManager();
        Assignments assignment1 = new Assignments("Homework 1", (int) System.currentTimeMillis()/1000, "medium", "MATH 1101", "", ""); // Priority score: 3 * .5 = 1.5
        Assignments assignment2 = new Assignments("Homework 2", (int) System.currentTimeMillis()/1000 + 86400 + 86400 , "low", "MATH 1101", "", ""); // Priority score: 1 * .25 = .25
        Assignments assignment3 = new Assignments("Homework 3", (int) System.currentTimeMillis()/1000 + (86400 * 12), "high", "MATH 1101", "", ""); // Priority score: .1 * 1 = .1
        Assignments assignment4 = new Assignments("Homework 4", (int) System.currentTimeMillis()/1000 + 86400, "medium", "MATH 1101", "", ""); // Priority score: 2 * .25 = .5

        manager.addAssignment(assignment1);
        manager.addAssignment(assignment2);
        manager.addAssignment(assignment3);
        manager.addAssignment(assignment4);


        try {
            assertEquals(assignment1, manager.getPriorityList().get(0));
            assertEquals(assignment4, manager.getPriorityList().get(1));
            assertEquals(assignment2, manager.getPriorityList().get(2));
            assertEquals(assignment3, manager.getPriorityList().get(3));
        } catch (Exception e) {
            fail(e + "Unexpected exception thrown");
        }

    }

    @Test
    public void testLoadData() {

        AssignmentsManager manager = new AssignmentsManager();

        Assignments assignment1 = new Assignments("Homework 1", 111111111, "medium", "MATH 1101", "link", ""); // Priority score: 3 * .5 = 1.5
        Assignments assignment2 = new Assignments("Homework 2", 111111111, "low", "MATH 1101", "", "test"); // Priority score: 1 * .25 = .25
        Assignments assignment3 = new Assignments("Homework 3", 111111111, "high", "MATH 1101", "link", "test"); // Priority score: .1 * 1 = .1

        manager.addAssignment(assignment1);
        manager.addAssignment(assignment2);
        manager.addAssignment(assignment3);

        AssignmentsManager newManager = new AssignmentsManager();

        manager.saveData();
        newManager.loadData();

        assertEquals(assignment1.getName(), newManager.getAllAssignments().get(0).getName());
        assertEquals(assignment2.getName(), newManager.getAllAssignments().get(1).getName());
        assertEquals(assignment3.getName(), newManager.getAllAssignments().get(2).getName());
    }

}
