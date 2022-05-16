package com.conjureimage.mtask.repository;

import com.conjureimage.mtask.domain.AppUser;
import com.conjureimage.mtask.domain.Board;
import com.conjureimage.mtask.domain.BoardAppUserRole;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BoardAppUserRoleRepository extends CrudRepository<BoardAppUserRole, Long> {
    BoardAppUserRole findByBoardAndAppUser(Board board, AppUser appUser);

}
