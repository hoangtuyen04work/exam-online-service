package edu.exam_online.exam_online_system.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.boot.actuate.audit.listener.AuditListener;

import java.time.LocalDateTime;
@EntityListeners(AuditListener.class)
@Entity
@Table(
    name = "role_permission",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"role_id", "permission_id"})}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
