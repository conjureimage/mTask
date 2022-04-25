package com.conjureimage.mtask.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "comment")
@Data
public class Comment extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", length = 2000)
    private String text;

    @ManyToOne
    private AppUser creator;

    @ManyToOne
    private Task task;

    @Override
    public Long getId() {
        return id;
    }
}