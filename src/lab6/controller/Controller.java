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

    //show available courses + free places
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

    /**
     *
     * @param studId id student
     * @return string profil
     */
    public String studProfile(int studId) {
        StringBuilder show = new StringBuilder();
        for (Student student : allStudents) {
            if (student.getStudentId() == studId) {
                show.append("ID: ").append(studId).append("\n")
                        .append("Name: ").append(student.getFirstName()). append(" ").append(student.getLastName()).append("\n")
                        .append("Credits: ").append(student.getTotalCredits()).append("\n")
                        .append("My courses: ");
                if (!student.getEnrolledCourses().isEmpty()) {

                    //LAMBDA EXPRESSION
                    student.getEnrolledCourses().forEach((course) -> {show.append(course.getName()).append("; ");});
                }
                show.append("\n");
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
                    show.append(", Teacher: ").append(teacher.getFirstName()).append(" ").append(teacher.getLastName());
                }
            }
            show.append(", Max enrollment: ").append(course.getMaxEnrollment()).append(", Credits: ").append(course.getCredits()).append("\n");

        }
        return show.toString();
    }


    public String showAllStudents() {
        StringBuilder show = new StringBuilder();
        StudentRepository studentRepository = new StudentRepository(allStudents);
        if (studentRepository.findAll() == null) {
            show.append("There are no students!");
            return show.toString();
        }
        for (Student student : studentRepository.findAll()) {
            show.append("ID: ").append(student.getStudentId()).append(", Name: ").append(student.getFirstName()).append(" ").append(student.getLastName());
            if (!student.getEnrolledCourses().isEmpty()) {
                show.append(", Enrolled in: ");
                for (Course course : student.getEnrolledCourses()) {
                    show.append(course.getName()).append("; ");
                }
            }
            show.append(" Credits: ").append(student.getTotalCredits()).append("\n");
        }
        return show.toString();
    }

    public String showAllTeachers() {
        StringBuilder show = new StringBuilder();
        if (allTeachers.isEmpty()) {
            show.append("There are no teachers!");
            return show.toString();
        }
        for (Teacher teacher : allTeachers) {
            show.append("Name: ").append(teacher.getFirstName()).append(" ").append(teacher.getLastName());
            if (!teacher.getCourses().isEmpty()) {
                show.append(", Courses: ");
                for (Course course : teacher.getCourses()) {
                    show.append(course.getName()).append("; ");
                }
            }
            show.append("\n");
        }
        return show.toString();
    }

    /**
     *
     * @param teacherId id teacher
     * @return string cu teacher's courses
     */
    public String showTeacherCourses(int teacherId) {
        Teacher teacher = new Teacher();
        for (Teacher teach : allTeachers) {
            if (teach.getTeacherId() == teacherId) {
                teacher = teach;
            }
        }
        StringBuilder show = new StringBuilder();
        if (teacher.getCourses().isEmpty()) {
            show.append("You do not teach any course");
            return show.toString();
        }
        for (Course course : teacher.getCourses()) {
            StudentRepository studentRepository = new StudentRepository(allStudents);
            show.append("Course: ").append(course.getName()).append("\n");
            for (int studId : course.getStudentsEnrolled()) {
                show.append(studentRepository.findOne(studId).getFirstName()).append(" ")
                        .append(studentRepository.findOne(studId).getLastName()).append("\n");
            }
            show.append("\n\n");
        }
        return show.toString();
    }

    /**
     *
     * @param courseName nume curs
     * @return dc e gresit => incorrect, altfel => studentii cursului
     */
    public String showStudFromCourse(String courseName) {
        StringBuilder show = new StringBuilder();
        boolean found = false;
        for (Course course : allCourses) {
            if (course.getName().equals(courseName)) {
                found = true;
                show.append("Course: ").append(course.getName()).append("\n");
                StudentRepository studentRepository = new StudentRepository(allStudents);
                for (int studId : course.getStudentsEnrolled()) {
                    show.append(studentRepository.findOne(studId).getFirstName()).append(" ")
                            .append(studentRepository.findOne(studId).getLastName()).append("\n");
                }
            }
        }
        if (!found) {
            show.append("incorrect");
        }
        return show.toString();
    }


    /**
     *
     * @param courseName nume curs
     * @param teacherId id teacher
     * @return incorrect dc nu s-a sters, altfel => deleted
     */
    public String deleteCourse(String courseName, int teacherId) {
        Teacher teacher = new Teacher();
        Course course = new Course();
        boolean found = false;
        for (Teacher teach : allTeachers) {
            if (teach.getTeacherId() == teacherId) {
                for (Course c : teach.getCourses()) {
                    if (c.getName().equals(courseName)) {
                        teacher = teach;
                        course = c;
                        found = true;
                    }
                }
            }
        }
        if (!found) {
            return "incorrect";
        }
        CourseRepository teacherCourses = new CourseRepository(teacher.getCourses());
        Course deletedCourse = teacherCourses.delete(course.getId());
        CourseRepository courses = new CourseRepository(allCourses);
        courses.delete(course.getId());
        if (deletedCourse != null) {
            course.setStudentsEnrolled(new ArrayList<>());
            for (Student student : allStudents) {
                student.getEnrolledCourses().remove(course);
            }
            return "deleted";
        }
        else {
            return "incorrect";
        }
    }
}
