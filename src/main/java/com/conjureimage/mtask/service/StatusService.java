package com.conjureimage.mtask.service;

import com.conjureimage.mtask.domain.Board;
import com.conjureimage.mtask.domain.Status;
import com.conjureimage.mtask.repository.BoardRepository;
import com.conjureimage.mtask.repository.StatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StatusService {
    private StatusRepository statusRepository;
    private BoardRepository boardRepository;


    public List<Status> findByBoardSlug(String slug) {
        Board board = boardRepository.findBySlug(slug);
        if (board == null) {
            return null;
        }
        return board.getStatuses();
    }

    public Status findByBoardSlugStatusId(String slug, Long id) {
        Status status = statusRepository.findById(id).orElse(null);
        if (status == null) {
            return null;
        }
        if (status.getBoard().getSlug().equals(slug)) {
            return status;
        }
        return null;
    }

    public Status createStatus(String slug, Status status) {
        Board board = boardRepository.findBySlug(slug);
        if (board == null) {
            return null;
        }
        status.setBoard(board);
        return statusRepository.save(status);
    }
}
