package com.conjureimage.mtask.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "task")
@Data
public class Task extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AppUser createdBy;

    @ManyToOne
    private AppUser assignedTo;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 5000)
    private String description;

    @ManyToOne
    private Status status;

    @OneToMany(mappedBy = "task")
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "position")
    private double position;

    @Override
    public Long getId() {
        return id;
    }
}