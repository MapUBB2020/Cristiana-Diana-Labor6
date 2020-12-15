package lab6.repository;

import lab6.model.Student;

import java.util.List;

public class StudentRepository implements ICrudRepository<Student> {
    protected List<Student> students;

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public StudentRepository(List<Student> students) {
        this.students = students;
    }

    @Override
    public Student findOne(Integer id) {
        if (!students.isEmpty()) {
            for (Student student : students) {
                if (student.getStudentId() == id) {
                    return student;
                }
            }
        }
        return null;
    }

    @Override
    public Iterable<Student> findAll() {
        if (!students.isEmpty()) {
            return students;
        }
        return null;
    }

    @Override
    public Student save(Student entity) {
        if (!students.contains(entity)) {
            students.add(entity);
            return entity;
        }
        return null;
    }

    @Override
    public Student delete(Integer id) {
        if (!students.isEmpty()) {
            for (Student student : students) {
                if (student.getStudentId() == id) {
                    students.remove(student);
                    return student;
                }
            }
        }
        return null;
    }

    @Override
    public Student update(Student entity) {
        if (!students.isEmpty()) {
            for (Student student : students) {
                if (student.getStudentId() == entity.getStudentId()) {
                    students.remove(student);
                    students.add(entity);
                    return entity;
                }

            }
        }
        return null;
    }

}
