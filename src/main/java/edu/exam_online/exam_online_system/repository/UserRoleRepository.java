package edu.exam_online.exam_online_system.repository;

import edu.exam_online.exam_online_system.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByUserIdAndRoleId(Long userId, Long roleId);

    List<UserRole> findByUserId(Long userId);
}
