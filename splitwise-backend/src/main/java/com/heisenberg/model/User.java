package com.heisenberg.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class User {
    @Id
    private int id;

    private boolean isDeleted;

    @CreatedDate
    @Column("create_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column("update_at")
    private Instant updatedAt;

    private String name;

    private String username;

    private String password;

    private boolean isActive;

    private String email;

    private String phone;

    @Column("profile_url")
    private String profileUrl;
}
