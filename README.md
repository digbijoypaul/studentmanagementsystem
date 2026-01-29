**Student Management System (Java Console App):**

A simple Java console application to manage student records with persistent file storage. This project demonstrates basic CRUD operations, layered architecture, and object serialization in Java.

**Overview:**

The Student Management System allows users to add, view, search, update, delete, and filter student records. All data is stored locally in a file (students.dat), ensuring records remain available even after restarting the application.

While developing this Student Management System, I gained strong practical experience in Java programming and real-world software design. This project helped me deepen my understanding of core Java concepts such as classes, objects, encapsulation, and modular programming by organizing the system into separate layers for entities, services, and data access.

I learned how to implement file handling and object serialization to store and retrieve student records persistently, which improved my understanding of how applications manage data beyond runtime. Designing CRUD operations strengthened my logical thinking and problem-solving skills, especially when handling edge cases like duplicate roll numbers, invalid input, and record updates.

Working on input validation and business rules taught me the importance of writing reliable and user-friendly software. I also improved my ability to structure code cleanly, making it more readable, maintainable, and scalable for future enhancements.

Overall, this project enhanced my confidence in building complete Java applications from scratch and gave me hands-on experience with software architecture, debugging, and practical implementation — preparing me for larger projects and real-world development scenarios.

**Features:**

1. Add new students with roll number, name, email, GPA, and course
2. View all registered students
3. Search students by roll number
4. Update existing student details
5. Delete student records
6. Filter students by course
7. View statistics such as average GPA and grade distribution
8. Automatic data saving and loading using file persistence

**Project Architecture:**

The application follows a layered structure for better maintainability:

**1. Student**
Entity class representing a student
Fields: Roll Number, Name, Email, GPA, Course
Implements Serializable for file storage

**2. StudentRepository**
Handles data storage and retrieval
CRUD operations
File input/output
Search and filter logic

**3. StudentService**
Contains business logic
Validates input data
Applies rules and constraints

**4. StudentManagementSystem**
Main class and console interface
Menu navigation
User interaction

**Requirements:**
Java 8 or later
No external libraries required

**How to Compile**
javac StudentManagementSystem.java

**How to Run**
java StudentManagementSystem

**Menu Options**
1. Add Student
2. View All Students
3. Search Student
4. Update Student
5. Delete Student
6. Filter by Course
7. View Statistics
8. Exit

**Data Storage:**

Student records are stored in students.dat
Data is automatically loaded at startup
Changes are saved instantly

**Project Structure**
StudentManagementSystem/
├── StudentManagementSystem.java
├── students.dat   (auto-generated)
└── README.md
