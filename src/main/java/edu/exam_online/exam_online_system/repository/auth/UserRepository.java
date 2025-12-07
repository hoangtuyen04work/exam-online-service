package edu.exam_online.exam_online_system.repository.auth;

import edu.exam_online.exam_online_system.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndRoleId(String email, Long roleId);

    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);

    boolean existsByEmailAndRoleId(String email, Long roleId);

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.role.name = :roleName")
    Optional<User> findByIdAndRoleName(Long id, String roleName);

    List<User> findAllByEmailInAndRoleName(List<String> emails, String roleName);

}
