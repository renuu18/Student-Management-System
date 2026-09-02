package web.management.service;

import org.springframework.data.domain.Page;
import web.management.entity.Student;

import java.util.Optional;

public interface StudentService {
    Optional<Student> findById(Long id);
    Iterable<Student> findAll();
    Page<Student> search(String keyword);
    Page<Student> findAll(int page, int size, String field, String direction);
    void save(Student student);
    void update(Student student, Long id);
    void deleteById(Long id);
    Optional<Student> findByEmail(String email);
}
