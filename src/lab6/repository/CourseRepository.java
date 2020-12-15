package lab6.repository;

import lab6.model.Course;

import java.util.List;

public class CourseRepository implements ICrudRepository<Course> {
    protected List<Course> courses;

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public CourseRepository(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public Course findOne(Integer id) {
        // daca lista de cursuri nu e goala => cauta cursul dupa id
        if (!courses.isEmpty()) {
            for (Course course : courses) {
                if (course.getId() == id) {
                    return course;
                }
            }
        }
        return null;
    }

    @Override
    public Iterable<Course> findAll() {
        // daca lista nu e goala => o returneaza
        if (!courses.isEmpty()) {
            return courses;
        }
        return null;
    }

    @Override
    public Course save(Course entity) {
        // daca lista nu contine cursul entity => il adauga
        if (!courses.contains(entity)) {
            courses.add(entity);
            return entity;
        }
        return null;
    }

    @Override
    public Course delete(Integer id) {
        // daca lista nu e goala si contine cursul cu id-ul id => il sterge
        if (!courses.isEmpty()) {
            for (Course course : courses) {
                if (course.getId() == id) {
                    courses.remove(course);
                    return course;
                }
            }
        }
        return null;
    }

    @Override
    public Course update(Course entity) {
        // daca lista nu e goala si gaseste cursul cu id = entity.id => sterge cursul si adauga entity in schimb
        if (!courses.isEmpty()) {
            for (Course course : courses) {
                if (course.getId() == entity.getId()) {
                    courses.remove(course);
                    courses.add(entity);
                    return entity;
                }

            }
        }
        return null;
    }
}
