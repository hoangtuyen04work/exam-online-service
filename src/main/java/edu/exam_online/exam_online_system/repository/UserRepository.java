package edu.exam_online.exam_online_system.repository;

import edu.exam_online.exam_online_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndRoleId(String email, Long roleId);

    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);

}
