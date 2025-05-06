package BLMS.repository;

import BLMS.model.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends MongoRepository<Admin, String> {
    Optional<Admin> findByEmail(String email);
    Optional<Admin> findByUsername(String username);

    List<Admin> findByEmailOrUsername(String email, String username);
}