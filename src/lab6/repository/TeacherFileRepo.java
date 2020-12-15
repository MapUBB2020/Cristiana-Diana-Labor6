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

public class TeacherFileRepo {
    public List<Teacher> allTeachers = new ArrayList<Teacher>();

    public TeacherFileRepo() {
    }

    public List<Teacher> readTeachers() throws FileNotFoundException {
        String line = " ";
        try {
            BufferedReader br = new BufferedReader(new FileReader("src\\lab6\\teachers.txt"));
            while ((line = br.readLine()) != null) {
                Teacher teacher = new Teacher();
                String[] obj = line.split(", ");
                teacher.setTeacherId(Integer.parseInt(obj[0]));
                teacher.setFirstName(obj[1]);
                teacher.setLastName(obj[2]);
                List<Course> courses = new ArrayList<Course>();
                teacher.setCourses(courses);
                allTeachers.add(teacher);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return allTeachers;
    }


}
