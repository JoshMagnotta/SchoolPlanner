package schoolplanner.assignmentWindow;
import java.util.*;
import java.time.*;
import java.io.*;

/**
 * Manager for Assignments
 */
public class AssignmentsManager {
    private ArrayList<Assignments> list;
    private static AssignmentsManager instance;


    public AssignmentsManager() {
        list = new ArrayList<>();
        loadData();
    }

    /**
     * Use Singleton properties
     * @return AssignmentsManager object
     */
    public static AssignmentsManager getInstance() {
        if (instance == null) {
            instance = new AssignmentsManager();
        }
        return instance;
    }

    public void addAssignment(Assignments assignment) {
        list.add(assignment);
        saveData();
    }

    public void editAssignment(Assignments assignment, String name, int dueDate, String priority, String subject, String link, String notes) {
        if (list.contains(assignment)) {
            if (!name.isBlank())
                assignment.setName(name);
            if (dueDate != 0)
                assignment.setDueDate(dueDate);
            if (!priority.isBlank())
                assignment.setPriority(priority);
            if (!subject.isBlank())
                assignment.setSubject(subject);
            if (!link.isBlank())
                assignment.setLink(link);
            if (!notes.isBlank())
                assignment.setNotes(notes);
        } else { // Assignment doesn't exist, add to list then edit any differences
            addAssignment(assignment);
            if (!name.isBlank())
                assignment.setName(name);
            if (dueDate != 0)
                assignment.setDueDate(dueDate);
            if (!priority.isBlank())
                assignment.setPriority(priority);
            if (!subject.isBlank())
                assignment.setSubject(subject);
            if (!link.isBlank())
                assignment.setLink(link);
            if (!notes.isBlank())
                assignment.setNotes(notes);
        }
        saveData();
    }

    public void removeAssignment(Assignments assignment) throws Exception {
        if (list.contains(assignment)) {
            list.remove(assignment);
            saveData();
            return;
        }

        throw new Exception("Could not find assignment");
    }

    public void setComplete(Assignments assignment) throws Exception {
        if(list.contains(assignment)) {
            assignment.setComplete(!assignment.isComplete());
            saveData();
            return;
        }

        throw new Exception("Could not find assignment");

    }

    public List<Assignments> getAllAssignments() {

        return list;
    }

    /**
     * Generates priority score based on when the assignment is due and the priority the user inputted
     * Score = User Priority * Due Date
     * @throws Exception if an invalid field is entered
     */
    public void generatePriority() throws Exception {
        double priorityScore = 0.0;
        for (Assignments assign : list) {
            // User priority score
            if (assign.getPriority().equals("High"))
                priorityScore = 10;
            else if (assign.getPriority().equals("Medium"))
                priorityScore = 5;
            else if (assign.getPriority().equals("Low"))
                priorityScore = 2.5;
            else
                throw new Exception("Invalid priority");

            long currentTime = System.currentTimeMillis()/1000;

            if (assign.getDueDate() - currentTime < 0) // overdue
                priorityScore = priorityScore * 30;
            else if (assign.getDueDate() - currentTime < 86400) // due in less than 24 hours
                priorityScore = priorityScore * 20;
            else if (assign.getDueDate() - currentTime < 172800) // due in less than 48 hours
                priorityScore = priorityScore * 10;
            else if (assign.getDueDate() - currentTime < 604800) // due in less than 7 days
                priorityScore = priorityScore * 5;
            else if (assign.getDueDate() - currentTime > 604800)// due in more than 7 days
                priorityScore = priorityScore * 1;
            else
                throw new Exception("Invalid time");

            assign.setPriorityScore(priorityScore); // update calculated priority score
        }

    }

    /**
     * Uses generatePriority to sort assignments in order from high to low
     * @return ArrayList in order of priority score
     * @throws Exception if generatePriority is given invalid fields
     */
    public ArrayList<Assignments> getPriorityList() throws Exception{
        generatePriority();
        ArrayList<Assignments> copyAssignments = new ArrayList<>(list);
        Collections.sort(copyAssignments, Comparator.comparingDouble(Assignments::getPriorityScore).reversed());

        return copyAssignments;
    }

    /**
     * Saves Assignment data to a CSV file
     */
    public void saveData() {
        try {
            File file = new File("saveAssignmentData.csv");
            if (file.createNewFile()) {
                System.out.println("CSV File created " + file.getName());
            } else {
                file.delete();
                file.createNewFile();
                System.out.println("Old CSV file deleted, new file created " + file.getName());
            }

            FileWriter writer = new FileWriter("saveAssignmentData.csv");
            for (Assignments assign : list) {
                String name = assign.getName();
                int dueDate = assign.getDueDate();
                String priority = assign.getPriority();
                String subject = assign.getSubject();
                String link = assign.getLink();
                String notes = assign.getNotes();
                boolean isComplete = assign.isComplete();

                // allows CSV to read null entries properly
                if (name.isBlank())
                    name = null;
                if (priority.isBlank())
                    priority = null;
                if (subject.isBlank())
                    subject = null;
                if (link.isBlank())
                    link = null;
                if (notes.isBlank())
                    notes = null;


                writer.write(name + "," + dueDate + "," + priority + "," + subject + "," + link + "," + notes + "," + isComplete + "\n");
            }

            writer.close();

        } catch (Exception e) {
            System.out.println(e + " Unexpected error thrown");
        }
    }


    /**
     * Loads data from a CSV file
     */
    // https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
    public void loadData() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("saveAssignmentData.csv"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                list.add(new Assignments(values[0], Integer.parseInt(values[1]), values[2], values[3], values[4], values[5], Boolean.parseBoolean(values[6])));
            }

            br.close();

        } catch (Exception e) {
            System.out.println(e + " unexpected exception thrown in loadData()");
        }

    }

}
