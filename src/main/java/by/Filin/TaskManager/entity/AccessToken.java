package by.Filin.TaskManager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "access_tokens")
public class AccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_valid", nullable = false)
    private boolean isValid = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt = new Date();

    @Column(name = "expires_at", nullable = false)
    private Date expiresAt;

}
