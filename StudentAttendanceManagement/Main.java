
import java.io.*;
import java.util.*;

class Student {
    int roll;
    String name;
    int classesHeld;
    int classesAttended;

    Student(int roll, String name) {
        this.roll = roll;
        this.name = name;
        this.classesHeld = 0;
        this.classesAttended = 0;
    }

    double getPercentage() {
        if (classesHeld == 0) {
            return 0.0;
        }

        return (classesAttended * 100.0) / classesHeld;
    }
}

public class Main {

    static ArrayList<Student> students = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        loadData();

        int choice=0;

        do {
            System.out.println("\n===== STUDENT ATTENDANCE MANAGEMENT =====");
            System.out.println("1. Add Student");
            System.out.println("2. Mark Attendance");
            System.out.println("3. Search Student");
            System.out.println("4. Defaulter List");
            System.out.println("5. Sort by Attendance");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
                continue;
            }

            switch (choice) {

                case 1:
                    addStudent();
                    break;

                case 2:
                    markAttendance();
                    break;

                case 3:
                    searchStudent();
                    break;

                case 4:
                    showDefaulters();
                    break;

                case 5:
                    sortStudents();
                    break;

                case 6:
                    saveData();
                    System.out.println("Data saved successfully!");
                    System.out.println("Thank you for using the system!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 6);

        sc.close();
    }

    // 1. Add Student

    static void addStudent() {

        System.out.print("Enter Roll Number: ");

        int roll;

        try {
            roll = Integer.parseInt(sc.nextLine());

            if (roll <= 0) {
                System.out.println("Roll number must be positive!");
                return;
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid roll number!");
            return;
        }

        for (Student s : students) {
            if (s.roll == roll) {
                System.out.println("Roll number already exists!");
                return;
            }
        }

        System.out.print("Enter Student Name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty() || name.contains("|")) {
            System.out.println("Invalid student name!");
            return;
        }

        Student s = new Student(roll, name);
        students.add(s);

        System.out.println("Student added successfully!");
        System.out.println(roll + " | " + name);

        saveData();
    }

    // 2. Mark Attendance

    static void markAttendance() {

        System.out.print("Enter Roll Number: ");

        int roll;

        try {
            roll = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid roll number!");
            return;
        }

        Student found = null;

        for (Student s : students) {
            if (s.roll == roll) {
                found = s;
                break;
            }
        }

        if (found == null) {
            System.out.println("Roll " + roll + " not found.");
            return;
        }

        System.out.print("Enter Attendance (P/A): ");

        String input = sc.nextLine().trim().toUpperCase();

        if (input.equals("P")) {

            found.classesHeld++;
            found.classesAttended++;

            System.out.println("Present marked successfully!");

        } else if (input.equals("A")) {

            found.classesHeld++;

            System.out.println("Absent marked successfully!");

        } else {

            System.out.println("Invalid input! Enter only P or A.");
            return;
        }

        System.out.println("Attendance: "
                + found.classesAttended + "/"
                + found.classesHeld);

        System.out.printf("Percentage: %.1f%%%n",
                found.getPercentage());

        saveData();
    }

    // 3. Search Student

    static void searchStudent() {

        System.out.print("Enter Roll Number: ");

        int roll;

        try {
            roll = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid roll number!");
            return;
        }

        for (Student s : students) {

            if (s.roll == roll) {

                System.out.println("\nStudent Found!");
                System.out.println("Roll: " + s.roll);
                System.out.println("Name: " + s.name);

                System.out.println("Attendance: "
                        + s.classesAttended + "/"
                        + s.classesHeld);

                System.out.printf("Percentage: %.1f%%%n",
                        s.getPercentage());

                return;
            }
        }

        System.out.println("Roll " + roll + " not found.");
    }

    // 4. Defaulter List

    static void showDefaulters() {

        System.out.println("\n===== DEFAULTER STUDENTS =====");

        boolean found = false;

        for (Student s : students) {

            if (s.getPercentage() < 75.0) {

                System.out.println(
                        s.roll + " | " +
                        s.name + " | " +
                        s.classesAttended + "/" +
                        s.classesHeld + " | " +
                        String.format("%.1f%%",
                                s.getPercentage())
                );

                found = true;
            }
        }

        if (!found) {
            System.out.println("No defaulter students found!");
        }
    }

    // 5. Sort Students by Attendance

    static void sortStudents() {

        if (students.isEmpty()) {
            System.out.println("No students available!");
            return;
        }

        students.sort((a, b) ->
                Double.compare(
                        b.getPercentage(),
                        a.getPercentage()
                )
        );

        System.out.println("\n===== SORTED STUDENT LIST =====");

        for (Student s : students) {

            System.out.printf(
                    "%d | %s | %d/%d | %.1f%%%n",
                    s.roll,
                    s.name,
                    s.classesAttended,
                    s.classesHeld,
                    s.getPercentage()
            );
        }
    }

    // 6. Load Data from File

    static void loadData() {

        File file = new File("students.txt");

        if (!file.exists()) {
            return;
        }

        try (BufferedReader br =
                     new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split("\\|", -1);

                if (data.length == 4) {

                    int roll = Integer.parseInt(data[0]);
                    String name = data[1];
                    int held = Integer.parseInt(data[2]);
                    int attended = Integer.parseInt(data[3]);

                    if (roll <= 0 || held < 0 ||
                            attended < 0 || attended > held ||
                            name.trim().isEmpty()) {
                        continue;
                    }

                    Student s = new Student(roll, name);

                    s.classesHeld = held;
                    s.classesAttended = attended;

                    boolean duplicate = false;

                    for (Student existing : students) {
                        if (existing.roll == roll) {
                            duplicate = true;
                            break;
                        }
                    }

                    if (!duplicate) {
                        students.add(s);
                    }
                }
            }

            System.out.println("Student data loaded successfully!");

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading student data!");
        }
    }

    // 7. Save Data to File

    static void saveData() {

        try (BufferedWriter bw =
                     new BufferedWriter(new FileWriter("students.txt"))) {

            for (Student s : students) {

                bw.write(
                        s.roll + "|" +
                        s.name + "|" +
                        s.classesHeld + "|" +
                        s.classesAttended
                );

                bw.newLine();
            }

        } catch (IOException e) {

            System.out.println("Error saving data!");
        }
    }
}
