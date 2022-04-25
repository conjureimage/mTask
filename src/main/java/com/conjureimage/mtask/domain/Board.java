package com.conjureimage.mtask.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "board")
@Data
public class Board extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "slug", length = 100, nullable = false, unique = true)
    private String slug;

    @OneToMany(mappedBy = "board")
    private List<BoardAppUserRole> boardAppUserRole = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<Status> statuses = new ArrayList<>();

    @Override
    public Long getId() {
        return id;
    }
}
