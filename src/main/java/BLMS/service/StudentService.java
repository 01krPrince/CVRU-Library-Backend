package BLMS.service;

import BLMS.model.Student;
import BLMS.model.request.StudentRegisterRequest;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student registerStudent(StudentRegisterRequest student);

    Optional<Student> findById(String id);

    List<Student> findAllStudents();

    Student updateStudent(String id, @Valid Student student);

    void deleteStudent(String id);

    List<Student> searchStudents(String email, String username);

    List<String> getBorrowHistory(String id);

    Student payFines(String id, double amount);

    Object loginUser(String email, String password);

    long totleStudentCount();
}
