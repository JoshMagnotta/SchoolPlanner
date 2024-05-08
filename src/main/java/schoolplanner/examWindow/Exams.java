package schoolplanner.examWindow;

/**
 * Creates Exams objects, tracked in ExamsManager
 */
public class Exams {
    private String name; // REQUIRED name of exam
    private String subject; // OPTIONAL subject the exam is for
    private int examTime; // REQUIRED time of the exam
    private String priority; // REQUIRED USER INPUTTED priority (high, medium, low)
    private String notes; // OPTIONAL additional notes about the exam
    private boolean isComplete; // returns T/F if exam is complete

    public Exams(String name, int examTime, String subject, String priority, String notes) {
        this.name = name;
        this.examTime = examTime;
        this.subject = subject;
        this.priority = priority;
        this.notes = notes;
        isComplete = false;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getExamTime() {
        return examTime;
    }

    public void setExamTime(int examTime) {
        this.examTime = examTime;
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
}
