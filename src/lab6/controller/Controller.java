package lab6.controller;

import lab6.model.Course;
import lab6.model.Student;
import lab6.model.Teacher;
import lab6.repository.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

//TODO: COMENTARII + JAVA DOCS

public class Controller {
    public List<Course> allCourses = new ArrayList<>();
    public List<Student> allStudents = new ArrayList<>();
    public List<Teacher> allTeachers = new ArrayList<>();
    public LogFileRepository logFileRepository;
    public CourseFileRepo courseFileRepo;
    public StudentFileRepo studentFileRepo;
    public TeacherFileRepo teacherFileRepo;

    public List<String> studEMails = new ArrayList<>();
    public List<String> studPasswords = new ArrayList<>();

    public List<String> teacherEMails = new ArrayList<>();
    public List<String> teacherPasswords = new ArrayList<>();

    public Controller() {
        logFileRepository = new LogFileRepository();
        courseFileRepo = new CourseFileRepo();
        studentFileRepo = new StudentFileRepo();
        teacherFileRepo = new TeacherFileRepo();
    }

    public void declareData() throws FileNotFoundException {
        allCourses = courseFileRepo.readCourses();
        allTeachers = courseFileRepo.teacherData();
        allStudents = courseFileRepo.studentData();
        logFileRepository.readAccounts();

        for (int i = 0; i < logFileRepository.eMails.size(); i++) {
            if ((logFileRepository.eMails.get(i).substring(logFileRepository.eMails.get(i).length() - 16)).equals("@stud.ubbcluj.ro")) {
                studEMails.add(logFileRepository.eMails.get(i));
                studPasswords.add(logFileRepository.passwords.get(i));
            }
            else {
                teacherEMails.add(logFileRepository.eMails.get(i));
                teacherPasswords.add(logFileRepository.passwords.get(i));
            }
        }
    }

    /**
     * ia ID-ul din parola
     * @param pswd parola de tip: ParolaID (ex: Parola123)
     * @return id student/teacher (ex: 123)
     */
    public int pswdToId(String pswd) {
        return Integer.parseInt(pswd.substring(6));
    }


    public String registerStud(int studId, String courseName) {
        Student student = new Student();
        Course course = new Course();
        for (Student std : allStudents) {
            if (std.getStudentId() == studId) {
                student = std;
            }
        }
        for (Course c : allCourses) {
            if (c.getName().equals(courseName)) {
                course = c;
            }
        }
        if (course.getStudentsEnrolled() == null) {
            return "incorrect";
        }
        if (course.getMaxEnrollment() - course.getStudentsEnrolled().size() > 0) {
            if (!student.getEnrolledCourses().contains(course)) {
                if (student.getTotalCredits() + course.getCredits() > 30) {
                    return "credits";
                }
                CourseRepository coursesFromStud = new CourseRepository(student.getEnrolledCourses());
                coursesFromStud.save(course);
                List<Integer> studentsFromCourse = course.getStudentsEnrolled();
                studentsFromCourse.add(student.getStudentId());
                return "enrolled";
            }
            else {
                return "already enrolled";
            }
        }
        else {
            return "full";
        }
    }


}
