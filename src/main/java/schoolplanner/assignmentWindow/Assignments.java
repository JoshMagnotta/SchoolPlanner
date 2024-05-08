package schoolplanner.assignmentWindow;

/**
 * Assignment objects created, controlled by AssignmentsManager
 */
public class Assignments {

    private String name; // REQUIRED name of assignment
    private String link; // OPTIONAL link to assignment (canvas, moodle, etc)
    private String notes; // OPTIONAL additional notes about the assignment
    private String subject; // OPTIONAL subject the assignment is for
    private int dueDate; // REQUIRED due date for the assignment
    private String priority; // REQUIRED USER INPUTTED priority (high, medium, low). Used for calculating priority list
    private boolean isComplete; // Keeps track of when an assignment is marked as do
    private double priorityScore; // Generated priority based on user priority and due date


    public Assignments() {
        name = null;
        dueDate = 0;
        priority = null;
        subject = null;
        link = null;
        notes = null;
        isComplete = false;
        priorityScore = 0.0;
    }

    public Assignments(String name, int dueDate, String priority, String subject, String link, String notes) {
        this.name = name;
        this.link = link;
        this.notes = notes;
        this.subject = subject;
        this.dueDate = dueDate;
        this.priority = priority;
        isComplete = false; // default to not complete
        priorityScore = 0.0; // will be generated when necessary
    }

    public Assignments(String name, int dueDate, String priority, String subject, String link, String notes, boolean isComplete) {
        this.name = name;
        this.link = link;
        this.notes = notes;
        this.subject = subject;
        this.dueDate = dueDate;
        this.priority = priority;
        this.isComplete = isComplete; // default to not complete
        priorityScore = 0.0; // will be generated when necessary
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getDueDate() {
        return dueDate;
    }

    public void setDueDate(int dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public void changeComplete() {
        isComplete = !isComplete; // reverse if complete or not
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPriorityScore() {
        return priorityScore;
    }

    public void setPriorityScore(double priorityScore) {
        this.priorityScore = priorityScore;
    }

    public int compareTo(Assignments other) {
        // Compare based on priority scores
        return Double.compare(other.getPriorityScore(), this.getPriorityScore());
    }
}
