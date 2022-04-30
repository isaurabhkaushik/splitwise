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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("groups")
public class Group {
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

    @Column("total_amount")
    private int totalAmount;

    private String txns;
}
