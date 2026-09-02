package web.management.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.management.entity.Student;
import web.management.payload.FilterPayload;
import web.management.service.StudentService;

@Controller
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        super();
        this.studentService = studentService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/legacy")
    public String legacy(Model model) {
        return findPaginated(1, 10, "id", "ASC", model);
    }

    @GetMapping("/students/details/{id}")
    public String details(@PathVariable("id") Long id, Model model) {
        Student student = studentService.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Invalid student Id:" + id));

        model.addAttribute("student", student);

        return "details";
    }

    @GetMapping("/students/{page}/{size}")
    public String findPaginated(
            @PathVariable int page,
            @PathVariable int size,
            @RequestParam(value = "field", defaultValue = "id") String field,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            Model model) {
        if (page < 1)
            page = 1;
        if (size < 1)
            size = 5;

        Page<Student> students = studentService.findAll(page, size, field, direction);
        Iterable<Student> studentList = students.getContent();

        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", students.getTotalPages());
        model.addAttribute("totalItems", students.getTotalElements());
        model.addAttribute("students", studentList);
        model.addAttribute("field", field);
        model.addAttribute("direction", direction);
        model.addAttribute("pagination", new FilterPayload());
        model.addAttribute("reverseDirection", direction.equalsIgnoreCase("ASC") ? "DESC" : "ASC");

        return "students";
    }

    @PostMapping("/students/filter/{page}")
    public String filter(
            @PathVariable int page,
            @RequestParam(value = "field", defaultValue = "id") String field,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @ModelAttribute("pagination") FilterPayload request,
            Model model
    ) {
        if (page < 1)
            page = 1;

        int size = request.getEntriesPerPage() == null || request.getEntriesPerPage().isEmpty() ? 5 : Integer.parseInt(request.getEntriesPerPage());
        Page<Student> students = studentService.findAll(page, size, field, direction);
        Iterable<Student> studentList = students.getContent();

        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", students.getTotalPages());
        model.addAttribute("totalItems", students.getTotalElements());
        model.addAttribute("students", studentList);
        model.addAttribute("field", field);
        model.addAttribute("direction", direction);
        model.addAttribute("pagination", request);
        model.addAttribute("reverseDirection", direction.equalsIgnoreCase("ASC") ? "DESC" : "ASC");

        return "students";
    }

    @GetMapping("/students/new")
    public String createStudentForm(Model model) {
        Student student = new Student();
        model.addAttribute("student", student);

        return "create_student";
    }

    @PostMapping("/students/search")
    public String searchStudent(
            @RequestParam("keyword") String keyword,
            Model model) {
        int page = 1;
        int size = 5;

        Page<Student> students = studentService.search(keyword);
        Iterable<Student> studentList = students.getContent();

        model.addAttribute("students", studentService.search(keyword));
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", students.getTotalPages());
        model.addAttribute("totalItems", students.getTotalElements());
        model.addAttribute("students", studentList);
        model.addAttribute("field", "id");
        model.addAttribute("direction", "ASC");
        model.addAttribute("pagination", new FilterPayload());
        model.addAttribute("reverseDirection", "DESC");

        return "students";
    }

    @GetMapping("/students/edit/{id}")
    public String editStudentForm(@PathVariable("id") Long id, Model model) {
        Student student = studentService.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Invalid student Id:" + id));

        model.addAttribute("student", student);

        return "edit_student";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudentForm(@PathVariable("id") Long id) {
        studentService.deleteById(id);

        return "index";
    }

    @PostMapping("/students")
    public String saveStudent(@ModelAttribute("student") Student student) {
        student.setPhotoUrl("https://www.w3schools.com/bootstrap4/img_avatar1.png");
        studentService.save(student);

        return "index";
    }

    @PostMapping("/students/{id}")
    public String updateStudent(
            @PathVariable("id") Long id, @ModelAttribute("student") Student student) {
        studentService.update(student, id);

        return "index";
    }
}