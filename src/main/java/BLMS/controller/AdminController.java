package BLMS.controller;

import BLMS.model.Admin;
import BLMS.model.request.AdminRegisterRequest;
import BLMS.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admins")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/register")
    public Admin registerAdmin(@Valid @RequestBody AdminRegisterRequest admin) {
        return adminService.registerAdmin(admin);
    }

    @GetMapping("/{id}")
    public Optional<Admin> getAdmin(@PathVariable String id) {
        return adminService.findById(id);
    }

    @GetMapping
    public List<Admin> getAllAdmins() {
        return adminService.findAllAdmins();
    }

    @PutMapping("/{id}")
    public Admin updateAdmin(@PathVariable String id, @Valid @RequestBody AdminRegisterRequest admin) {
        return adminService.updateAdmin(id, admin);
    }

    @DeleteMapping("/{id}")
    public void deleteAdmin(@PathVariable String id) {
        adminService.deleteAdmin(id);
    }

    @GetMapping("/login")
    public List<Admin> loginAdmins(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username) {
        return adminService.loginAdmins(email, username);
    }

}