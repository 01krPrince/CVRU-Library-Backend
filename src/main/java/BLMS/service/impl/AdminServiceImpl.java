package BLMS.service.impl;

import BLMS.model.Admin;
import BLMS.model.request.AdminRegisterRequest;
import BLMS.repository.AdminRepository;
import BLMS.service.AdminService;
import BLMS.service.BookService;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private StudentServiceImpl studentService;

    @Override
    public Admin registerAdmin(AdminRegisterRequest admin) {
        // Validate input
        if (admin == null) {
            throw new IllegalArgumentException("Admin cannot be null");
        }
        if (adminRepository.findByEmail(admin.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (adminRepository.findByUsername(admin.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Create new admin
        Admin newAdmin = new Admin();
        newAdmin.setUsername(admin.getUsername());
        newAdmin.setFullName(admin.getFullName());
        newAdmin.setEmail(admin.getEmail());
        newAdmin.setPassword(admin.getPassword());
        newAdmin.setRole("ADMIN");
        newAdmin.setPhone(admin.getPhone());
        newAdmin.setRegistrationDate(LocalDate.now());

        return adminRepository.save(newAdmin);
    }

    @Override
    public Optional<Admin> findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        return adminRepository.findById(id);
    }

    @Override
    public List<Admin> findAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public Admin updateAdmin(String id, AdminRegisterRequest admin) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (admin == null) {
            throw new IllegalArgumentException("Admin cannot be null");
        }

        // Find existing admin
        Admin existingAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Admin not found with ID: " + id));

        // Check for email/username conflicts
        Optional<Admin> adminWithEmail = adminRepository.findByEmail(admin.getEmail());
        if (adminWithEmail.isPresent() && !adminWithEmail.get().getId().equals(id)) {
            throw new IllegalArgumentException("Email already exists");
        }
        Optional<Admin> adminWithUsername = adminRepository.findByUsername(admin.getUsername());
        if (adminWithUsername.isPresent() && !adminWithUsername.get().getId().equals(id)) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Update fields
        existingAdmin.setUsername(admin.getUsername());
        existingAdmin.setFullName(admin.getFullName());
        existingAdmin.setEmail(admin.getEmail());
        if (admin.getPassword() != null && !admin.getPassword().trim().isEmpty()) {
            existingAdmin.setPassword(admin.getPassword()); // Hash password
        }
        existingAdmin.setPhone(admin.getPhone());

        return adminRepository.save(existingAdmin);
    }

    @Override
    public void deleteAdmin(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (!adminRepository.existsById(id)) {
            throw new NoSuchElementException("Admin not found with ID: " + id);
        }
        adminRepository.deleteById(id);
    }

    @Override
    public List<Admin> loginAdmins
            (String email, String username) {
        if (email == null && username == null) {
            return adminRepository.findAll();
        }
        return adminRepository.findByEmailOrUsername(email, username);
    }

}
