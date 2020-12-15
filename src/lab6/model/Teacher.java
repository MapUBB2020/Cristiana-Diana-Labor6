package lab6.model;

import java.util.List;

public class Teacher extends Person {
    private int teacherId;
    private List<Course> courses;

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public Teacher(int teacherId, String firstName, String lastName, List<Course> courses) {
        super(firstName, lastName);
        this.teacherId = teacherId;
        this.courses = courses;
    }

    public Teacher() {

    }

    @Override
    public String toString() {
        StringBuilder c = new StringBuilder();
        for (Course course : courses) {
            if (course == courses.get(courses.size() - 1)) {
                c.append(course.getName());
            } else {
                c.append(course.getName()).append(", ");
            }
        }
        return "Teacher{" +
                "ID = " + getTeacherId() +
                ", firstName = " + getFirstName() +
                ", lastName = " + getLastName() +
                ", courses = " + c +
                '}';
    }
}
