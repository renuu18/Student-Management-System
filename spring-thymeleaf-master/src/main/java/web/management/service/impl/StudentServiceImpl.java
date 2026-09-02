package web.management.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import web.management.entity.Student;
import web.management.repository.StudentRepository;
import web.management.service.StudentService;

import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    @Override
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    public Iterable<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public void save(Student student) {
        studentRepository.save(student);
    }

    @Override
    public Page<Student> search(String keyword) {
        Sort sorted = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(0, 5, sorted);

        return studentRepository.search(keyword, pageable);
    }

    @Override
    public Page<Student> findAll(int page, int size, String field, String direction) {
        Sort sorted = direction.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(field).ascending()
                : Sort.by(field).descending();
        Pageable pageable = PageRequest.of(page - 1, size, sorted);

        return studentRepository.findAll(pageable);
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    @Override
    public void update(Student student, Long id) {
        Student fetchedStudent = studentRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Student not found")
        );

        fetchedStudent.setFirstName(student.getFirstName());
        fetchedStudent.setLastName(student.getLastName());
        fetchedStudent.setEmail(student.getEmail());
        fetchedStudent.setAge(student.getAge());

        studentRepository.save(fetchedStudent);
    }

    @Override
    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }
}
