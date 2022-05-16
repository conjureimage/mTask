package com.conjureimage.mtask.service;

import com.conjureimage.mtask.domain.AppUser;
import com.conjureimage.mtask.domain.Board;
import com.conjureimage.mtask.domain.BoardAppUserRole;
import com.conjureimage.mtask.domain.enums.AppUserRole;
import com.conjureimage.mtask.exception.UserNotAuthenticated;
import com.conjureimage.mtask.repository.AppUserRepository;
import com.conjureimage.mtask.repository.BoardAppUserRoleRepository;
import com.conjureimage.mtask.repository.BoardRepository;
import com.conjureimage.mtask.security.utils.SecurityUtil;
import com.conjureimage.mtask.service.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BoardService {
    private AppUserRepository appUserRepository;
    private BoardRepository boardRepository;
    private BoardAppUserRoleRepository boardAppUserRoleRepository;

    public List<Board> findByUser(AppUser appUser) {
        return boardRepository.findByBoardAppUserRoleAppUser(appUser);
    }

    public Board findBySlug(String slug) {
        return boardRepository.findBySlug(slug);
    }

    public Board createBoard(String title, HttpServletRequest request) throws UserNotAuthenticated {
        Board board = new Board();
        board.setTitle(title);
        String slug = StringUtils.randomString(8);
        while (findBySlug(slug) != null) {
            slug = StringUtils.randomString(8);
        }
        board.setSlug(slug);
        board = boardRepository.save(board);
        BoardAppUserRole boardAppUserRole = new BoardAppUserRole();
        boardAppUserRole.setBoard(board);
        boardAppUserRole.setAppUser(SecurityUtil.getUserDetails(request));
        boardAppUserRole.setAppUserRole(AppUserRole.ROLE_ADMIN);
        boardAppUserRoleRepository.save(boardAppUserRole);
        List<BoardAppUserRole> list = new ArrayList<>();
        list = board.getBoardAppUserRole();
        list.add(boardAppUserRole);
        board.setBoardAppUserRole(list);
        System.out.println(list);
        return board;
    }

    public Board updateBoard(Board board) {
        Board myBoard = boardRepository.findBySlug(board.getSlug());
        if (myBoard == null) {
            return null;
        }
        myBoard.setTitle(board.getTitle());
        return boardRepository.save(myBoard);
    }

    public List<BoardAppUserRole> addUserToBoard(String slug, AppUser user, AppUserRole appUserRole, HttpServletRequest request) throws UserNotAuthenticated {
        Board myBoard = boardRepository.findBySlug(slug);
        AppUser appUser = appUserRepository.findByEmail(user.getEmail());
        if (myBoard == null
                || appUser == null
                || appUserRole == null) {
            return null;
        }

        AppUser me = SecurityUtil.getUserDetails(request);
        for (BoardAppUserRole baur :
                myBoard.getBoardAppUserRole()) {

            if (me.getId() == baur.getAppUser().getId() && baur.getAppUserRole() == AppUserRole.ROLE_USER) {
                return myBoard.getBoardAppUserRole();
            }
        }

        BoardAppUserRole boardAppUserRole = boardAppUserRoleRepository.findByBoardAndAppUser(myBoard, appUser);
        if (boardAppUserRole == null) {
            boardAppUserRole = new BoardAppUserRole();
            boardAppUserRole.setBoard(myBoard);
            boardAppUserRole.setAppUser(appUser);
        }

        boolean adminToUser = false;
        int admins = 0;

        for (BoardAppUserRole baur :
                myBoard.getBoardAppUserRole()) {

            if (baur.getAppUserRole() == AppUserRole.ROLE_ADMIN && baur.getAppUser().getId() == appUser.getId() && appUserRole == AppUserRole.ROLE_USER) {
                adminToUser = true;
            }
            if (baur.getAppUserRole() == AppUserRole.ROLE_ADMIN) {
                admins++;
            }
        }

        if (admins == 0 || (admins == 1 && adminToUser)) {
            boardAppUserRole.setAppUserRole(AppUserRole.ROLE_ADMIN);
        } else {
            boardAppUserRole.setAppUserRole(appUserRole);
        }

        boardAppUserRoleRepository.save(boardAppUserRole);
        return myBoard.getBoardAppUserRole();
    }

    public boolean deleteUserFromBoard(AppUser currentUser, String slug, Long boardUserRole) {
        Board board = findBySlug(slug);

        boolean canDelete = false;

        int admins = 0;

        BoardAppUserRole boardUserRoleToDelete = null;

        for (BoardAppUserRole baur :
                board.getBoardAppUserRole()) {
            if (baur.getAppUser().getId() == currentUser.getId() && baur.getAppUserRole() == AppUserRole.ROLE_ADMIN) {
                canDelete = true;
            }

            if (baur.getAppUserRole() == AppUserRole.ROLE_ADMIN) {
                admins++;
            }

            if (baur.getId() == boardUserRole) {
                boardUserRoleToDelete = baur;
            }
        }

        if (!canDelete) {
            return false;
        }

        if (boardUserRoleToDelete != null && boardUserRoleToDelete.getAppUserRole() == AppUserRole.ROLE_ADMIN && admins < 2) {
            return false;
        }

        boardAppUserRoleRepository.delete(boardUserRoleToDelete);
        return true;
    }
}
