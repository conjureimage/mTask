package com.conjureimage.mtask.controller;

import com.conjureimage.mtask.domain.Status;
import com.conjureimage.mtask.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boards/{slug}/statuses")
public class StatusController {
    StatusService statusService;

    @GetMapping
    public ResponseEntity<List<Status>> getStatuses(@PathVariable String slug) {
        List<Status> sprints = statusService.findByBoardSlug(slug);
        if (sprints == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(sprints, HttpStatus.OK);
    }

    @GetMapping(value = "/{sprintId}")
    public ResponseEntity<Status> getSprint(@PathVariable String slug, @PathVariable Long sprintId) {
        Status status = statusService.findByBoardSlugStatusId(slug, sprintId);
        if (status == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Status> createSprint(@PathVariable String slug, @RequestBody Status status) {
        Status newStatus = statusService.createStatus(slug, status);
        if (newStatus == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(newStatus, HttpStatus.CREATED);
    }
}
