package edu.exam_online.exam_online_system.utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import edu.exam_online.exam_online_system.entity.Role;
import edu.exam_online.exam_online_system.entity.User;
import edu.exam_online.exam_online_system.exception.AppException;
import edu.exam_online.exam_online_system.exception.ErrorCode;
import edu.exam_online.exam_online_system.repository.TokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static edu.exam_online.exam_online_system.commons.constant.TimeConstant.JWT_EXPIRATION_MS;

@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@RequiredArgsConstructor
@Component
public class TokenUtils {

    TokenRepository tokenRepository;

    String secret = "18x30Q0l4Sdr783UnbNl6drrmqSobLJB"; // >= 32 chars


    private JWSSigner getSigner(){
        try{
            return new MACSigner(secret.getBytes(StandardCharsets.UTF_8));
        }
        catch (Exception e){
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private JWSVerifier getVerifier() {
        try{
            return new MACVerifier(secret.getBytes(StandardCharsets.UTF_8));
        }
        catch (Exception e){
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Sinh Access Token
     */
    public String generateToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + JWT_EXPIRATION_MS);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issueTime(now)
                .expirationTime(expiry)
                .claim("roles", extractRoles(user))
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claimsSet
        );

        try{
            signedJWT.sign(getSigner());
        }
        catch (Exception e){
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return signedJWT.serialize();
    }

    /**
     * Sinh Refresh Token
     */
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * Validate token
     */
    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            if (!signedJWT.verify(getVerifier())) {
                return false;
            }

            if(!tokenRepository.existsByToken(token)){
                throw new AppException(ErrorCode.NOT_AUTHENTICATION);
            }

            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            String username = signedJWT.getJWTClaimsSet().getSubject();

            return expiration != null
                    && expiration.after(new Date());

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extract username
     */
    public String extractUsername(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new RuntimeException("Invalid token", e);
        }
    }

    /**
     * Extract expiration
     */
    public Date extractExpiration(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getExpirationTime();
        } catch (ParseException e) {
            throw new RuntimeException("Invalid token", e);
        }
    }

    /**
     * Check expired
     */
    public boolean isTokenExpired(String token) {
        Date exp = extractExpiration(token);
        return exp.before(new Date());
    }

    public List<String> extractRoles(User user) {
        Role role = user.getRole();
        return List.of(role.getName());
    }

}