package BLMS.controller;

import BLMS.model.Student;
import BLMS.model.request.StudentRegisterRequest;
import BLMS.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
@CrossOrigin("*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/register-student")
    public Student registerStudent(@RequestBody StudentRegisterRequest student) {
        return studentService.registerStudent(student);
    }

    @GetMapping("login")
    public Object loginStudent(@RequestParam String email, @RequestParam String password){
        return studentService.loginUser(email,password);
    }

    @GetMapping("/{id}")
    public Optional<Student> getStudent(@PathVariable String id) {
        return studentService.findById(id);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.findAllStudents();
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable String id, @Valid @RequestBody Student student) {
        return studentService.updateStudent(id, student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable String id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("/search")
    public List<Student> searchStudents(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username) {
        return studentService.searchStudents(email, username);
    }

    @GetMapping("/{id}/borrow-history")
    public List<String> getBorrowHistory(@PathVariable String id) {
        return studentService.getBorrowHistory(id);
    }

    @PostMapping("/{id}/pay-fines")
    public Student payFines(@PathVariable String id, @RequestParam double amount) {
        return studentService.payFines(id, amount);
    }
}