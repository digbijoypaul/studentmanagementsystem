import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Student class - represents a student entity
class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    private String rollNumber;
    private String name;
    private String email;
    private double gpa;
    private String course;

    public Student(String rollNumber, String name, String email, double gpa, String course) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.email = email;
        this.gpa = gpa;
        this.course = course;
    }

    // Getters and Setters
    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }
    
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    @Override
    public String toString() {
        return String.format("Roll: %s | Name: %-20s | Email: %-25s | GPA: %.2f | Course: %s",
                rollNumber, name, email, gpa, course);
    }
}

// StudentRepository - handles data storage and retrieval
class StudentRepository {
    private static final String FILE_PATH = "students.dat";
    private List<Student> students;

    public StudentRepository() {
        this.students = new ArrayList<>();
        loadFromFile();
    }

    // CREATE
    public boolean addStudent(Student student) {
        if (findByRollNumber(student.getRollNumber()) != null) {
            System.out.println("Student with Roll Number already exists!");
            return false;
        }
        students.add(student);
        saveToFile();
        return true;
    }

    // READ
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public Student findByRollNumber(String rollNumber) {
        return students.stream()
                .filter(s -> s.getRollNumber().equals(rollNumber))
                .findFirst()
                .orElse(null);
    }

    public List<Student> findByCourse(String course) {
        List<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (s.getCourse().equalsIgnoreCase(course)) {
                result.add(s);
            }
        }
        return result;
    }

    // UPDATE
    public boolean updateStudent(String rollNumber, Student updatedStudent) {
        Student existing = findByRollNumber(rollNumber);
        if (existing == null) {
            System.out.println("Student not found!");
            return false;
        }
        existing.setName(updatedStudent.getName());
        existing.setEmail(updatedStudent.getEmail());
        existing.setGpa(updatedStudent.getGpa());
        existing.setCourse(updatedStudent.getCourse());
        saveToFile();
        return true;
    }

    // DELETE
    public boolean deleteStudent(String rollNumber) {
        Student student = findByRollNumber(rollNumber);
        if (student == null) {
            System.out.println("Student not found!");
            return false;
        }
        students.remove(student);
        saveToFile();
        return true;
    }

    // File Operations
    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            students = (List<Student>) ois.readObject();
            System.out.println("Data loaded from file (" + students.size() + " records)");
        } catch (FileNotFoundException e) {
            System.out.println("No existing data file found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(students);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public int getStudentCount() {
        return students.size();
    }

    public double getAverageGpa() {
        if (students.isEmpty()) return 0;
        return students.stream()
                .mapToDouble(Student::getGpa)
                .average()
                .orElse(0);
    }
}

// StudentService - business logic
class StudentService {
    private StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public void createStudent(String rollNumber, String name, String email, double gpa, String course) {
        Student student = new Student(rollNumber, name, email, gpa, course);
        if (repository.addStudent(student)) {
            System.out.println("Student added successfully!");
        }
    }

    public void displayAllStudents() {
        List<Student> students = repository.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        System.out.println("\n" + "=".repeat(100));
        System.out.println("STUDENT LIST");
        System.out.println("=".repeat(100));
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i));
        }
        System.out.println("=".repeat(100) + "\n");
    }

    public void searchStudent(String rollNumber) {
        Student student = repository.findByRollNumber(rollNumber);
        if (student != null) {
            System.out.println("\n Student Found:\n" + student + "\n");
        } else {
            System.out.println("\n Student not found!\n");
        }
    }

    public void updateStudent(String rollNumber) {
        Student existing = repository.findByRollNumber(rollNumber);
        if (existing == null) {
            System.out.println(" Student not found!");
            return;
        }
        
        Scanner sc = new Scanner(System.in);
        System.out.println("Current Details: " + existing);
        System.out.print("Enter new name: ");
        String name = sc.nextLine();
        System.out.print("Enter new email: ");
        String email = sc.nextLine();
        System.out.print("Enter new GPA: ");
        double gpa = Double.parseDouble(sc.nextLine());
        System.out.print("Enter new course: ");
        String course = sc.nextLine();
        
        Student updated = new Student(rollNumber, name, email, gpa, course);
        if (repository.updateStudent(rollNumber, updated)) {
            System.out.println("Student updated successfully!");
        }
    }

    public void deleteStudent(String rollNumber) {
        if (repository.deleteStudent(rollNumber)) {
            System.out.println("Student deleted successfully!");
        }
    }

    public void displayStatistics() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("STATISTICS");
        System.out.println("=".repeat(50));
        System.out.println("Total Students: " + repository.getStudentCount());
        System.out.println("Average GPA: " + String.format("%.2f", repository.getAverageGpa()));
        System.out.println("=".repeat(50) + "\n");
    }

    public void filterByCourse(String course) {
        List<Student> filtered = repository.findByCourse(course);
        if (filtered.isEmpty()) {
            System.out.println("No students found in course: " + course);
            return;
        }
        System.out.println("\n" + "=".repeat(100));
        System.out.println("STUDENTS IN COURSE: " + course);
        System.out.println("=".repeat(100));
        for (int i = 0; i < filtered.size(); i++) {
            System.out.println((i + 1) + ". " + filtered.get(i));
        }
        System.out.println("=".repeat(100) + "\n");
    }
}

// Main Application - Console UI
public class StudentManagementSystem {
    private static StudentService service;
    private static Scanner scanner;

    public static void main(String[] args) {
        StudentRepository repository = new StudentRepository();
        service = new StudentService(repository);
        scanner = new Scanner(System.in);
        System.out.println("\n");
        System.out.println("STUDENT MANAGEMENT SYSTEM");

        boolean running = true;
        while (running) {
            displayMenu();
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addStudent();
                    break;
                case "2":
                    service.displayAllStudents();
                    break;
                case "3":
                    searchStudentMenu();
                    break;
                case "4":
                    updateStudentMenu();
                    break;
                case "5":
                    deleteStudentMenu();
                    break;
                case "6":
                    filterByCourseMenu();
                    break;
                case "7":
                    service.displayStatistics();
                    break;
                case "8":
                    System.out.println("\n Thank you for using Student Management System!");
                    running = false;
                    break;
                default:
                    System.out.println(" Invalid choice! Please try again.\n");
            }
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("1. Add Student");
        System.out.println("2. View All Students");
        System.out.println("3. Search Student");
        System.out.println("4. Update Student");
        System.out.println("5. Delete Student");
        System.out.println("6. Filter by Course");
        System.out.println("7. View Statistics");
        System.out.println("8. Exit");
    }

    private static void addStudent() {
        System.out.println("\n--- Add New Student ---");
        System.out.print("Roll Number: ");
        String rollNumber = scanner.nextLine().trim();
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("GPA: ");
        double gpa = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Course (CSE/ECE/ME/CE): ");
        String course = scanner.nextLine().trim();

        service.createStudent(rollNumber, name, email, gpa, course);
        System.out.println();
    }

    private static void searchStudentMenu() {
        System.out.print("\nEnter Roll Number to search: ");
        String rollNumber = scanner.nextLine().trim();
        service.searchStudent(rollNumber);
    }

    private static void updateStudentMenu() {
        System.out.print("\nEnter Roll Number to update: ");
        String rollNumber = scanner.nextLine().trim();
        service.updateStudent(rollNumber);
        System.out.println();
    }

    private static void deleteStudentMenu() {
        System.out.print("\nEnter Roll Number to delete: ");
        String rollNumber = scanner.nextLine().trim();
        service.deleteStudent(rollNumber);
        System.out.println();
    }

    private static void filterByCourseMenu() {
        System.out.print("\nEnter Course (CSE/ECE/ME/CE): ");
        String course = scanner.nextLine().trim();
        service.filterByCourse(course);
    }
}
