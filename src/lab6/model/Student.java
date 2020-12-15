package lab6.model;

import java.util.List;

public class Student extends Person implements Comparable<Student> {

    private int studentId;
    private int totalCredits;
    private List<Course> enrolledCourses;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getTotalCredits() {
        // calculare credite; se actualizeaza cand se apeleaza nr de credite, daca nr de credite al unui curs se schimba
        int credits = 0;
        for (Course course : enrolledCourses) {
            credits += course.getCredits();
        }
        totalCredits = credits;
        return totalCredits;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(List<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public Student() {
        super();

    }

    public Student(String firstName, String lastName, Integer studentId, List<Course> enrolledCourses) {
        super(firstName, lastName);
        this.studentId = studentId;
        this.enrolledCourses = enrolledCourses;
    }

    @Override
    public String toString() {
        StringBuilder courses = new StringBuilder();
        for (Course course : enrolledCourses) {
            if (course == enrolledCourses.get(enrolledCourses.size() - 1)) {
                courses.append(course.getName());
            }
            else {
                courses.append(course.getName()).append(", ");
            }
        }
        return "Student{" +
                "studentId = " + studentId +
                ", firstName = " + getFirstName() +
                ", lastName = " + getLastName() +
                ", totalCredits = " + getTotalCredits() +
                ", enrolledCourses = " + courses +
                '}';
    }

    //functie care compara numele studentiilor
    @Override
    public int compareTo(Student o) {
        return this.getLastName().compareTo(o.getLastName());
    }
}

