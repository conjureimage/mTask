package com.conjureimage.mtask.controller;

import com.conjureimage.mtask.domain.Comment;
import com.conjureimage.mtask.domain.Task;
import com.conjureimage.mtask.exception.UserNotAuthenticated;
import com.conjureimage.mtask.security.utils.SecurityUtil;
import com.conjureimage.mtask.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boards/{slug}/statuses/{statusId}/tasks")
public class TaskController {
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(@PathVariable String slug, @PathVariable Long statusId) {
        List<Task> tasks = taskService.findByBoardAndSprint(slug, statusId);
        if (tasks == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping(value = "/{taskId}")
    public ResponseEntity<Task> getTask(@PathVariable String slug, @PathVariable Long statusId, @PathVariable Long taskId) {
        Task task = taskService.findOne(slug, statusId, taskId);
        if (task == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@PathVariable String slug, @PathVariable Long statusId, @RequestBody Task task) throws UserNotAuthenticated {
        Task newTask = taskService.createTask(slug, statusId, task);
        if (newTask == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (newTask.getCreatedBy().getId() != newTask.getAssignedTo().getId()) {
            String url = "#/b/" + slug + "/tasks/" + statusId + "-" + newTask.getId();
        }
        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable String slug, @PathVariable Long statusId, @PathVariable Long taskId, @RequestBody Task task) {
        Task newTask = taskService.updateTask(slug, statusId, taskId, task);
        if (newTask == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }

    @PostMapping(value = "/{taskId}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable String slug, @PathVariable Long statusId, @PathVariable Long taskId, @RequestBody String comment) throws UserNotAuthenticated {
        Comment newComment = taskService.addComment(slug, statusId, taskId, comment, SecurityUtil.getUserDetails());
        if (newComment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String url = "#/b/" + slug + "/tasks/" + statusId + "-" + taskId;
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }
}
