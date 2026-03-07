package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userId; // Added at [24:10]

    private String name;

    @Column(unique = true)
    private String email; // Marked unique at [23:04]

    private String password;

    private String verifyOTP;

    private boolean isAccountVerified;

    private Date verifyOTPExpiredAt;

    private String resetOTP;

    private Date resetOTPExpiredAt;

    @CreationTimestamp
    @Column(updatable = false) // [23:28]
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;
}
