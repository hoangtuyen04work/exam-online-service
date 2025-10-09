package edu.exam_online.exam_online_system.repository;

import edu.exam_online.exam_online_system.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {

    Optional<Code> findByCodeAndUserId(String code, Long user_id);
}
