package web.management.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.management.entity.Student;
import web.management.service.StudentService;

import java.text.MessageFormat;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentsRestController {
    private final StudentService studentService;

    @GetMapping
    public List<Student> findAll() {
        return (List<Student>) studentService.findAll();
    }

    @GetMapping("/{email}")
    public Student findByEmail(@PathVariable String email) {
        return studentService.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException(MessageFormat.format("No student with given email was found: {0}", email)
                ));
    }

    @PostMapping
    public ResponseEntity<Student> create(@RequestBody Student student) {
        student.setPhotoUrl("https://www.w3schools.com/bootstrap4/img_avatar1.png");
        studentService.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @RequestBody Student student) {
        studentService.update(student, id);
        return studentService.findById(id).orElseThrow(
                () -> new IllegalArgumentException(MessageFormat.format("No student with given id was found: {0}", id))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
