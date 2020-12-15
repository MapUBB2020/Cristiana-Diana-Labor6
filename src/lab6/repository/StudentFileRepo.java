package lab6.repository;

import lab6.model.Course;
import lab6.model.Student;
import lab6.model.Teacher;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentFileRepo {
    public List<Student> allStudents = new ArrayList<Student>(); //lista cu toti studentii

    public StudentFileRepo() {
    }

    public List<Student> readStudents() throws FileNotFoundException {
        String line = " ";
        try {
            BufferedReader br = new BufferedReader(new FileReader("src\\lab6\\students.txt"));
            while ((line = br.readLine()) != null) {
                Student student = new Student();
                String[] obj = line.split(", ");
                student.setStudentId(Integer.parseInt(obj[0]));
                student.setFirstName(obj[1]);
                student.setLastName(obj[2]);
                student.setTotalCredits(Integer.parseInt(obj[3]));
                List<Course> courses = new ArrayList<Course>();
                student.setEnrolledCourses(courses);
                allStudents.add(student);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allStudents;
    }
}
