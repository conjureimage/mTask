package com.conjureimage.mtask.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "status")
@Data
public class Status extends BaseEntity<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne
    @JsonIgnore
    private Board board;

    @OneToMany(mappedBy = "status")
    private List<Task> tasks = new ArrayList<>();

    @Override
    public Long getId() {
        return id;
    }
}
