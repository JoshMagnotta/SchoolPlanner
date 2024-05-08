package schoolplanner.examWindow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Tracker for Exams
 */
public class ExamsManager {
    private ArrayList<Exams> list;

    private static ExamsManager instance;

    public ExamsManager() {
        list = new ArrayList<>();
        loadExamData();
    }

    /**
     * Singleton design pattern
     * @return signle ExamsManager object
     */
    public static ExamsManager getInstance() {
        if (instance == null) {
            instance = new ExamsManager();
        }
        return instance;
    }

    public void addExam(Exams exam) {
        list.add(exam);
        saveExamData();
    }

    public void editExam(Exams exam, String name, int examTime, String subject, String priority, String notes) {
        if (list.contains(exam)) {
            if (!name.isBlank())
                exam.setName(name);
            if (examTime != 0)
                exam.setExamTime(examTime);
            if (!priority.isBlank())
                exam.setPriority(priority);
            if (!subject.isBlank())
                exam.setSubject(subject);
            if (!notes.isBlank())
                exam.setNotes(notes);
        } else { // Assignment doesn't exist, add to list then edit any differences
            addExam(exam);
            if (!name.isBlank())
                exam.setName(name);
            if (examTime != 0)
                exam.setExamTime(examTime);
            if (!priority.isBlank())
                exam.setPriority(priority);
            if (!subject.isBlank())
                exam.setSubject(subject);
            if (!notes.isBlank())
                exam.setNotes(notes);
        }
        saveExamData();
    }

    public void removeExam(Exams exam) throws Exception {
        if (list.contains(exam)) {
            list.remove(exam);
            saveExamData();

            return;
        }

        throw new Exception("Could not find assignment");
    }

    public List<Exams> getAllExams() {
        return list;
    }

    /**
     * Saves Assignment data to a CSV file
     */
    public void saveExamData() {
        try {
            File file = new File("SaveExamData.csv");
            if (file.createNewFile()) {
                System.out.println("CSV File created " + file.getName());
            } else {
                file.delete();
                file.createNewFile();
                System.out.println("Old CSV file deleted, new file created " + file.getName());
            }

            FileWriter writer = new FileWriter("SaveExamData.csv");
            for (Exams exams : list) {
                String name = exams.getName();
                String subject = exams.getSubject();
                int examTime = exams.getExamTime();
                String priority = exams.getPriority();
                String notes = exams.getNotes();

                if (name.isBlank())
                    name = null;
                if (priority.isBlank())
                    priority = null;
                if (subject.isBlank())
                    subject = null;
                if (notes.isBlank())
                    notes = null;

                writer.write(name + "," + examTime + "," + subject + "," + priority + "," + notes + "\n");
            }

            writer.close();

        } catch (Exception e) {
            System.out.println(e + " Unexpected error thrown");
        }
    }


    /**
     * Loads data from a CSV file
     */
    public void loadExamData() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("SaveExamData.csv"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                list.add(new Exams(values[0], Integer.parseInt(values[1]), values[2], values[3], values[4]));
            }

            br.close();

        } catch (Exception e) {
            System.out.println(e + " unexpected exception thrown in loadData()");
        }

    }

}
