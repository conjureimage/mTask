package com.conjureimage.mtask.domain;

import com.conjureimage.mtask.domain.enums.AppUserRole;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "board_user_role")
@Data
public class BoardAppUserRole extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Board board;

    @ManyToOne
    @JoinColumn(name = "board_user")
    private AppUser appUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20, nullable = false)
    private AppUserRole appUserRole;

    @Override
    public Long getId() {
        return id;
    }
}
