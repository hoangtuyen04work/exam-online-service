package edu.exam_online.exam_online_system.repository;

import edu.exam_online.exam_online_system.entity.Token;
import edu.exam_online.exam_online_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);
    Optional<Token> findByRefreshToken(String refreshToken);
    List<Token> findAllByUserAndIsRevokedFalse(User user);
    boolean existsByToken(String token);
}
