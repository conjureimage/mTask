package com.conjureimage.mtask.controller;

import com.conjureimage.mtask.config.JwtCoder;
import com.conjureimage.mtask.domain.AppUser;
import com.conjureimage.mtask.domain.Board;
import com.conjureimage.mtask.domain.BoardAppUserRole;
import com.conjureimage.mtask.domain.enums.AppUserRole;
import com.conjureimage.mtask.exception.UserNotAuthenticated;
import com.conjureimage.mtask.security.utils.SecurityUtil;
import com.conjureimage.mtask.service.BoardService;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/boards")
@AllArgsConstructor
public class BoardController {
    BoardService boardService;

    @GetMapping
    public ResponseEntity<Iterable<Board>> getBoards(HttpServletRequest request) throws UserNotAuthenticated {
        return new ResponseEntity<>(boardService.findByUser(SecurityUtil.getUserDetails(request)), HttpStatus.OK);
    }

    @GetMapping(value = "/{slug}")
    public ResponseEntity<Board> boardBySlug(@PathVariable String slug) {
        Board board = boardService.findBySlug(slug);
        if (board != null) {
            return new ResponseEntity<>(board, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Board> saveBoard(@RequestBody String name, HttpServletRequest request) throws UserNotAuthenticated {
        return new ResponseEntity<>(boardService.createBoard(name, request), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{slug}")
    public ResponseEntity<Board> editBoard(@PathVariable String slug, @RequestBody Board board) {
        if (slug.equals(board.getSlug())) {
            Board myBoard = boardService.updateBoard(board);
            if (myBoard != null) {
                return new ResponseEntity<>(boardService.updateBoard(board), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/{slug}/users")
    public ResponseEntity<List<BoardAppUserRole>> addAppUserToBoard(@PathVariable String slug, @RequestBody AppUser user, HttpServletRequest request) throws UserNotAuthenticated {
        return new ResponseEntity<>(boardService.addUserToBoard(slug, user, AppUserRole.ROLE_USER, request), HttpStatus.OK);
    }

    @PutMapping(value = "/{slug}/users")
    public ResponseEntity<List<BoardAppUserRole>> editAppUserInBoard(@PathVariable String slug, @RequestBody BoardAppUserRole boardAppUserRole, HttpServletRequest request) throws UserNotAuthenticated {
        List<BoardAppUserRole> newBoardAppUserRole = boardService.addUserToBoard(slug, boardAppUserRole.getAppUser(), boardAppUserRole.getAppUserRole(), request);
        boolean removed = true;
        boolean isAdmin = false;
        for (BoardAppUserRole baur :
                newBoardAppUserRole) {
            if (baur.getAppUser().getId() == boardAppUserRole.getAppUser().getId()) {
                removed = false;
                if (baur.getAppUserRole() == AppUserRole.ROLE_ADMIN) {
                    isAdmin = true;
                }
            }
        }
        return new ResponseEntity<>(newBoardAppUserRole, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{slug}/users/{boardAppUserRole}")
    public ResponseEntity<Boolean> deleteAppUserInBoard(@PathVariable String slug, @PathVariable Long boardAppUserRole, HttpServletRequest request) throws UserNotAuthenticated {
        boolean deleted = boardService.deleteUserFromBoard(SecurityUtil.getUserDetails(request), slug, boardAppUserRole);
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }
}
