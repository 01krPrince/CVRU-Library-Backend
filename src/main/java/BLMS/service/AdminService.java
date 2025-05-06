package BLMS.service;

import BLMS.model.Admin;
import BLMS.model.request.AdminRegisterRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AdminService {
    Admin registerAdmin(AdminRegisterRequest admin);
    Optional<Admin> findById(String id);
    List<Admin> findAllAdmins();
    Admin updateAdmin(String id, AdminRegisterRequest admin);
    void deleteAdmin(String id);
    List<Admin> loginAdmins(String email, String username);

}