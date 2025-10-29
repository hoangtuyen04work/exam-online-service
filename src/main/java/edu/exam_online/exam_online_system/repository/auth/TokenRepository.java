package edu.exam_online.exam_online_system.repository.auth;

import edu.exam_online.exam_online_system.entity.auth.Token;
import edu.exam_online.exam_online_system.entity.auth.User;
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
