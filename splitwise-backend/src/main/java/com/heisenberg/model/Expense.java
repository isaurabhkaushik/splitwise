package com.heisenberg.model;

import com.heisenberg.domain.Share;
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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("expenses")
public class Expense {
    @Id
    private int id;

    private boolean isDeleted;

    @CreatedDate
    @Column("create_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column("update_at")
    private Instant updatedAt;

    private int groupId;

    private int totalAmount;

    private String receiptUrl;

    private int createdBy;

    private String shares;
}
