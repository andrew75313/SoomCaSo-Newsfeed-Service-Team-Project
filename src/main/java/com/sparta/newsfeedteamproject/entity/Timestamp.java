package com.sparta.newsfeedteamproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Timestamp {

    @CreatedDate
    @Column(nullable = false, name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(nullable = false, name = "modified_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modifiedAt;
}
