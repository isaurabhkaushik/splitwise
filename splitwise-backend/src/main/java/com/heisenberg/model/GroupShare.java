package com.heisenberg.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("group_share")
public class GroupShare {
    @Id
    private int id;

    private boolean isDeleted;

    @CreatedDate
    @Column("create_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column("update_at")
    private Instant updatedAt;

    private int userId;

    private int groupId;

    private boolean isSettled;

    private int totalPaid;

    private int totalOwed;

    public GroupShare(int userId, int groupId) {
        this.userId = userId;
        this.groupId = groupId;
        this.setCreatedAt(new Timestamp(System.currentTimeMillis()).toInstant());
        this.setUpdatedAt(new Timestamp(System.currentTimeMillis()).toInstant());
    }
}
