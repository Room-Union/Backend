package org.codeit.roomunion.user.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity(name = "email_verifications")
@Getter
public class EmailVerificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expirationAt;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean verified;

    protected EmailVerificationEntity() {
    }

    public static EmailVerificationEntity of(String email, String code, LocalDateTime currentAt, LocalDateTime expirationAt) {
        EmailVerificationEntity entity = new EmailVerificationEntity();
        entity.email = email;
        entity.code = code;
        entity.createdAt = currentAt;
        entity.expirationAt = expirationAt;
        entity.verified = false;
        return entity;
    }

    public void verify() {
        verified = true;
    }

    public boolean isCodeNotValid(String code) {
        return verified || !this.code.equals(code);
    }

    public void renewExpirationAt(LocalDateTime expirationAt) {
        this.expirationAt = expirationAt;
    }
}
