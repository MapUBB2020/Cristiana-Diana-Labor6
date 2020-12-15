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

public class CourseFileRepo {
    public List<Course> allCourses = new ArrayList<Course>(); //lista cu toate cursurile
    public List<Student> allStudents = new ArrayList<Student>(); //lista cu toti studentii
    public List<Teacher> allTeachers = new ArrayList<Teacher>();

    public TeacherFileRepo teacherFileRepo = new TeacherFileRepo();
    public StudentFileRepo studentFileRepo = new StudentFileRepo();

    public List<Course> readCourses() throws FileNotFoundException {
        allStudents = studentFileRepo.readStudents();
        allTeachers = teacherFileRepo.readTeachers();
        String line = " ";
        try {
            BufferedReader br = new BufferedReader(new FileReader("src\\lab6\\courses.txt"));
            while ((line = br.readLine()) != null) {
                List<Integer> studentsFromCourse = new ArrayList<Integer>();
                List<Integer> ids = new ArrayList<Integer>();
                Course course = new Course();
                String[] obj = line.split(", ");
                course.setId(Integer.parseInt(obj[0]));
                course.setName(obj[1]);
                for (Teacher teacher : allTeachers) {
                    if (teacher.getTeacherId() == Integer.parseInt(obj[2])) {
                        course.setTeacher(teacher.getTeacherId());
                    }
                }
                course.setMaxEnrollment(Integer.parseInt(obj[3]));
                String[] id = obj[4].split("; ");
                for (int i = 0; i < id.length; i++) {
                    if (id.length == 1) {
                        ids.add(Integer.parseInt(id[0].substring(1, 3)));
                    } else if (i == 0) {
                        ids.add(Integer.parseInt(id[i].substring(1)));
                    } else if (i == id.length - 1) {
                        ids.add(Integer.parseInt(id[i].substring(0, 3)));
                    } else {
                        ids.add(Integer.parseInt(id[i]));
                    }
                }
                for (int studId : ids) {
                    for (Student student : allStudents) {
                        if (student.getStudentId() == studId) {
                            studentsFromCourse.add(studId);
                        }
                    }
                }
                course.setStudentsEnrolled(studentsFromCourse);
                course.setCredits(Integer.parseInt(obj[5]));
                allCourses.add(course);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return allCourses;
    }

    public List<Teacher> teacherData() {
        for (Teacher teacher : allTeachers) {
            for (Course course : allCourses) {
                if (teacher.getTeacherId() == course.getTeacher()) {
                    List<Course> courses = teacher.getCourses();
                    courses.add(course);
                    teacher.setCourses(courses);
                }
            }
        }
        return allTeachers;
    }

    public List<Student> studentData() {
        for (Student student : allStudents) {
            for (Course course : allCourses) {
                for (Integer studId : course.getStudentsEnrolled()) {
                    if (student.getStudentId() == studId) {
                        List<Course> courses = student.getEnrolledCourses();
                        courses.add(course);
                        student.setEnrolledCourses(courses);
                    }
                }
            }
        }
        return allStudents;
    }
}
