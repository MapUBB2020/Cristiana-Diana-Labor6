package lab6.model;

import java.util.List;

public class Course {
    private int id;
    private String name;
    private int teacher;
    private int maxEnrollment;
    private List<Integer> studentsEnrolled;
    private int credits;

    public Course() {

    }

    public Course(int id, String name, int teacher, int maxEnrollment, List<Integer> studentsEnrolled, int credits) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
        this.maxEnrollment = maxEnrollment;
        this.studentsEnrolled = studentsEnrolled;
        this.credits = credits;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTeacher() {
        return teacher;
    }

    public void setTeacher(int teacher) {
        this.teacher = teacher;
    }

    public int getMaxEnrollment() {
        return maxEnrollment;
    }

    public void setMaxEnrollment(int maxEnrollment) {
        this.maxEnrollment = maxEnrollment;
    }

    public List<Integer> getStudentsEnrolled() {
        return studentsEnrolled;
    }

    public void setStudentsEnrolled(List<Integer> studentsEnrolled) {
        this.studentsEnrolled = studentsEnrolled;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    @Override
    public String toString() {
        StringBuilder students = new StringBuilder();
        for (long studentid : studentsEnrolled) {
            if (studentid == studentsEnrolled.get(studentsEnrolled.size() - 1)) {
                students.append(studentid).append(" ");
            }
            else {
                students.append(studentid).append(", ");
            }
        }
        return "Course{" +
                "id = " + id +
                ", name = '" + name + '\'' +
                ", teacher = " + teacher +
                ", maxEnrollment = " + maxEnrollment +
                ", studentsEnrolled = " + students +
                ", credits = " + credits +
                '}';
    }
}

