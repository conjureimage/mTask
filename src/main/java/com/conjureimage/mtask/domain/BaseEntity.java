package com.conjureimage.mtask.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity<ID> implements Serializable {
    @Column(name = "creation_time", nullable = false)
    private LocalDateTime creationTime;

    @Column(name = "modification_time", nullable = false)
    private LocalDateTime modificationTime;

    @Version
    private long version;

    public abstract ID getId();

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public LocalDateTime getModificationTime() {
        return modificationTime;
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.creationTime = now;
        this.modificationTime = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.modificationTime = LocalDateTime.now();
    }

}
