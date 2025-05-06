package BLMS.service.impl;

import BLMS.model.Admin;
import BLMS.model.Student;
import BLMS.model.request.StudentRegisterRequest;
import BLMS.repository.AdminRepository;
import BLMS.repository.UserRepository;
import BLMS.service.StudentService;
import org.bson.types.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Student registerStudent(StudentRegisterRequest student) {

        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        if (userRepository.findByEmail(student.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        String enrollmentNo = student.getEnrollmentNo();

        if (enrollmentNo != null && enrollmentNo.matches("CVB21\\d{5}")) {
            int numericPart = Integer.parseInt(enrollmentNo.substring(5));
            if (numericPart >= 1 && numericPart <= 99999) { // CVB2500001 to CVB2599999
                if (userRepository.findById(enrollmentNo).isPresent()) {
                    throw new IllegalArgumentException("Enrollment number already exists");
                }
            } else {
                throw new IllegalArgumentException("Invalid enrollment number1");
            }
        } else {
            throw new IllegalArgumentException("Invalid enrollment number2");
        }

        Student newStudent = new Student();
        newStudent.setEnrollmentNo(student.getEnrollmentNo());
        newStudent.setFullName(student.getFullName());
        newStudent.setEmail(student.getEmail());
        newStudent.setPassword(student.getPassword());
        newStudent.setRole("STUDENT");
        newStudent.setPhone(student.getPhone());
        newStudent.setTotalFines(0.0);
        newStudent.setBorrowRecordIds(new ArrayList<>());
        newStudent.setStatus("ACTIVE");

        return userRepository.save(newStudent);
    }

    @Override
    public Object loginUser(String email, String password) {
        // First try to find a student
        Optional<Student> studentOpt = userRepository.findByEmail(email);

        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (student.getPassword().equals(password)) {
                return student;
            } else {
//                throw new RuntimeException("Invalid student password");
            }
        }

        Optional<Admin> adminOpt = adminRepository.findByEmail(email);

        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (admin.getPassword().equals(password)) {
                return admin;
            } else {
                throw new RuntimeException("Invalid admin password");
            }
        }

        throw new RuntimeException("User not found");
    }

    @Override
    public Optional<Student> findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        return userRepository.findById(id);
    }

    @Override
    public List<Student> findAllStudents() {
        return userRepository.findAll();
    }

    @Override
    public Student updateStudent(String id, Student student) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }

        Student existingStudent = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Student not found with ID: " + id));

        Optional<Student> studentWithEmail = userRepository.findByEmail(student.getEmail());
        if (studentWithEmail.isPresent() && !studentWithEmail.get().getEnrollmentNo().equals(id)) {
            throw new IllegalArgumentException("Email already exists");
        }
        Optional<Student> studentWithEnrollmentNo = userRepository.findById(student.getEnrollmentNo());
        if (studentWithEnrollmentNo.isPresent() && !studentWithEnrollmentNo.get().getEnrollmentNo().equals(id)) {
            throw new IllegalArgumentException("Enrollment number already exists");
        }

        existingStudent.setEnrollmentNo(student.getEnrollmentNo());
        existingStudent.setFullName(student.getFullName());
        existingStudent.setEmail(student.getEmail());
        if (student.getPassword() != null && !student.getPassword().trim().isEmpty()) {
            existingStudent.setPassword(student.getPassword()); // Should hash password in production
        }
        existingStudent.setPhone(student.getPhone());
        existingStudent.setStatus(student.getStatus());

        return userRepository.save(existingStudent);
    }

    @Override
    public void deleteStudent(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("Student not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<Student> searchStudents(String email, String username) {
        if (email == null) {
            return userRepository.findAll();
        }
        return userRepository.findByEmail(email).map(List::of).orElse(new ArrayList<>());
    }

    @Override
    public List<String> getBorrowHistory(String id) {
//        Student student = userRepository.findById(id)
//                .orElseThrow(() -> new NoSuchElementException("Student not found with ID: " + id));
//        return student.getBorrowRecordIds() != null ? student.getBorrowRecordIds() : new ArrayList<>();
        return new ArrayList<>();
    }

    @Override
    public Student payFines(String id, double amount) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Student student = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Student not found with ID: " + id));
        double currentFines = student.getTotalFines();
        if (currentFines <= 0) {
            throw new IllegalStateException("No fines to pay");
        }
        if (amount > currentFines) {
            throw new IllegalArgumentException("Payment amount exceeds total fines");
        }

        student.setTotalFines(currentFines - amount);
        return userRepository.save(student);
    }

    public long totleStudentCount() {
        return userRepository.count();
    }
}