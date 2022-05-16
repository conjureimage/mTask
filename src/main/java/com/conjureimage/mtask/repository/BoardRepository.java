package com.conjureimage.mtask.repository;

import com.conjureimage.mtask.domain.AppUser;
import com.conjureimage.mtask.domain.Board;
import com.conjureimage.mtask.domain.BoardAppUserRole;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BoardRepository extends CrudRepository<Board, Long> {
    Board findBySlug(String slug);

    List<Board> findByBoardAppUserRoleAppUser(AppUser appUser);

}
