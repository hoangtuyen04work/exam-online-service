package edu.exam_online.exam_online_system.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.boot.actuate.audit.listener.AuditListener;

import java.time.LocalDateTime;
import java.util.Set;

@EntityListeners(AuditListener.class)
@Entity
@Table(name = "`user`")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( unique = true, length = 50)
    private String username;

    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 255)
    private String password;

    @Column(length = 50)
    private String firstName;

    @Column( length = 50)
    private String lastName;

    @Column(length = 20)
    private String phone;

    @Column()
    @Builder.Default
    private Boolean isActive = true;

    @Column()
    @Builder.Default
    private Boolean isEmailVerified = false;

    private LocalDateTime lastLogin;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Token> tokens;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Code> codes;
}
