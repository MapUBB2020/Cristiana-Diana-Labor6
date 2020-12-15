package lab6.controller;

import lab6.model.Course;
import lab6.model.Student;
import lab6.model.Teacher;
import lab6.repository.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    public List<Course> allCourses = new ArrayList<>(); //lista cu toate cursurile
    public List<Student> allStudents = new ArrayList<>(); //lista cu toti studentii
    public List<Teacher> allTeachers = new ArrayList<>(); //lista cu toti profesorii
    public LogFileRepository logFileRepository;
    public CourseFileRepo courseFileRepo;
    public StudentFileRepo studentFileRepo;
    public TeacherFileRepo teacherFileRepo;

    public List<String> studEMails = new ArrayList<>(); //lista cu toate eMail-urile studentilor
    public List<String> studPasswords = new ArrayList<>(); //lista cu toate parolele studentilor

    public List<String> teacherEMails = new ArrayList<>(); //lista cu toate eMail-urile profesorilor
    public List<String> teacherPasswords = new ArrayList<>(); //lista cu toate parolele profesorilor

    //constructor
    public Controller() {
        logFileRepository = new LogFileRepository();
        courseFileRepo = new CourseFileRepo();
        studentFileRepo = new StudentFileRepo();
        teacherFileRepo = new TeacherFileRepo();
    }

    public void declareData() throws FileNotFoundException {
        //citim datele din fisier pentru courses/students/teachers/accounts
        allCourses = courseFileRepo.readCourses();
        allTeachers = courseFileRepo.teacherData();
        allStudents = courseFileRepo.studentData();
        logFileRepository.readAccounts();

        //cautam in lista cu toate eMail-urile
        for (int i = 0; i < logFileRepository.eMails.size(); i++) {
            //daca eMail-ul are la sfarsit "@stud.ubbcluj.ro" => e student
            if ((logFileRepository.eMails.get(i).substring(logFileRepository.eMails.get(i).length() - 16)).equals("@stud.ubbcluj.ro")) {
                studEMails.add(logFileRepository.eMails.get(i)); //adaugam eMail-ul la lista de eMail-uri pentru studenti
                studPasswords.add(logFileRepository.passwords.get(i)); //adaugam parola la lista de parole pentru studenti
            } else {
                //daca eMail-ul nu are la sfarsit "@stud.ubbcluj.ro" => e profesor
                teacherEMails.add(logFileRepository.eMails.get(i)); //adaugam eMail-ul la lista de eMail-uri pentru profesori
                teacherPasswords.add(logFileRepository.passwords.get(i)); //adaugam parola la lista de parole pentru profesori
            }
        }
    }

    /**
     * ia ID-ul din parola
     *
     * @param pswd parola de tip: ParolaID (ex: Parola123)
     * @return id student/teacher (ex: 123)
     */
    public int pswdToId(String pswd) {
        return Integer.parseInt(pswd.substring(6));
    }

    /**
     * enrol a student in a course
     *
     * @param studId     - id-ul studentului
     * @param courseName - numele cursului
     * @return - "incorrect" daca nu a pus datele corect, "credits" daca studentul are mai mult de 30 de credite,
     * "enrolled" daca studentul a fost inregistrat cu succes, "already enrolled" daca studentul e deja inscris la cursul ales,
     * "full" daca cursul ales nu mai are locuri libere
     */
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
        //verificam daca cursul mai are locuri libere
        if (course.getMaxEnrollment() - course.getStudentsEnrolled().size() > 0) {
            //verificam daca studentul nu e deja inscris la curs
            if (!student.getEnrolledCourses().contains(course)) {
                //verificam daca studentul are mai mult de 30 de credite
                if (student.getTotalCredits() + course.getCredits() > 30) {
                    return "credits";
                }
                CourseRepository coursesFromStud = new CourseRepository(student.getEnrolledCourses());
                coursesFromStud.save(course); //salvam cursul in lista cu cursuri ale studentului
                List<Integer> studentsFromCourse = course.getStudentsEnrolled();
                studentsFromCourse.add(student.getStudentId()); //salvam studentul in lista cu studenti ale cursului
                return "enrolled";
            } else {
                return "already enrolled";
            }
        } else {
            return "full";
        }
    }

    public String showAvailableCourses() {
        StringBuilder show = new StringBuilder();
        CourseRepository courseRepository = new CourseRepository(allCourses);
        if (courseRepository.findAll() == null) {
            show.append("There are no courses!");
            return show.toString();
        }
        for (Course course : courseRepository.findAll()) {
            int available = course.getMaxEnrollment() - course.getStudentsEnrolled().size();
            if (available > 0) {
                if (available == 1) {
                    show.append(course.getName()).append(": ").append(available).append(" free place\n");
                } else {
                    show.append(course.getName()).append(": ").append(available).append(" free places\n");
                }
            }
        }
        return show.toString();
    }

    public String showAllCourses() {
        StringBuilder show = new StringBuilder();
        CourseRepository courseRepository = new CourseRepository(allCourses);
        if (courseRepository.findAll() == null) {
            show.append("There are no courses!");
            return show.toString();
        }
        for (Course course : courseRepository.findAll()) {
            show.append(course.getName());
            for (Teacher teacher : allTeachers) {
                if (teacher.getTeacherId() == course.getTeacher()) {
                    show.append(", teacher: ").append(teacher.getFirstName()).append(" ").append(teacher.getLastName()).append("\n");
                }
            }

        }
        return show.toString();
    }

    public String showMyCourses(int studentId) {
        Student student = new Student();
        for (Student stud : allStudents) {
            if (stud.getStudentId() == studentId) {
                student = stud;
            }
        }
        StringBuilder show = new StringBuilder();
        if (student.getEnrolledCourses().isEmpty()) {
            show.append("You are not enrolled in any course");
            return show.toString();
        }
        for (Course course : student.getEnrolledCourses()) {
            show.append(course.getName()).append("\n");
        }
        return show.toString();
    }

}
