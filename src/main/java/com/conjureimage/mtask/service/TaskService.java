package com.conjureimage.mtask.service;

import com.conjureimage.mtask.domain.AppUser;
import com.conjureimage.mtask.domain.Comment;
import com.conjureimage.mtask.domain.Status;
import com.conjureimage.mtask.domain.Task;
import com.conjureimage.mtask.exception.UserNotAuthenticated;
import com.conjureimage.mtask.repository.AppUserRepository;
import com.conjureimage.mtask.repository.CommentRepository;
import com.conjureimage.mtask.repository.StatusRepository;
import com.conjureimage.mtask.repository.TaskRepository;
import com.conjureimage.mtask.security.utils.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TaskService {
    private StatusRepository statusRepository;
    private TaskRepository taskRepository;
    private AppUserRepository appUserRepository;
    private CommentRepository commentRepository;


    public List<Task> findByBoardAndSprint(String slug, Long sprintId) {
        Status status = statusRepository.findById(sprintId).orElse(null);
        if (status == null || !status.getBoard().getSlug().equals(slug)) {
            return null;
        }
        return taskRepository.findByStatusOrderByPosition(status);
    }

    public Task findOne(String slug, Long statusId, Long taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            return null;
        }
        if (!Objects.equals(task.getStatus().getId(), statusId)
                || ! task.getStatus().getBoard().getSlug().equals(slug)) {
            return null;
        }
        return task;
    }

    public Task createTask(String slug, Long statusId, String title, String description, HttpServletRequest request) {
        Status status = statusRepository.findById(statusId).orElse(null);
        if (status == null || !status.getBoard().getSlug().equals(slug)) {
            return null;
        }
        Task task = new Task();
        task.setStatus(status);
        task.setTitle(title);
        task.setDescription(description);
        AppUser user;
        user = SecurityUtil.getUserDetails(request);
        task.setCreatedBy(user);
        if (task.getAssignedTo() == null) {
            task.setAssignedTo(user);
        } else {
            AppUser au = appUserRepository.findByEmail(task.getAssignedTo().getEmail());
            if (au != null) {
                task.setAssignedTo(au);
            } else {
                task.setAssignedTo(user);
            }
        }
        task.setPosition(0f);
        task = taskRepository.save(task);
        task.setPosition(2f * task.getId());
        return taskRepository.save(task);
    }

    public Task updateTask(String slug, Long sprintId, Long taskId, Task task) {
        Task oldTask = taskRepository.findById(taskId).orElse(null);
        if (oldTask == null
                || !Objects.equals(oldTask.getStatus().getId(), sprintId)
                || !oldTask.getStatus().getBoard().getSlug().equals(slug)) {
            return null;
        }
        oldTask.setTitle(task.getTitle());
        if (task.getAssignedTo() != null
                && !Objects.equals(oldTask.getAssignedTo().getId(), task.getAssignedTo().getId())) {
            AppUser au = appUserRepository.findByEmail(task.getAssignedTo().getEmail());
            if (au != null) {
                oldTask.setAssignedTo(au);
            }
        }
        oldTask.setDescription(task.getDescription());
        oldTask.setPosition(task.getPosition());
        return taskRepository.save(oldTask);
    }

    public Comment addComment(String slug, Long sprintId, Long taskId, String text, AppUser taskCreator) {
        Task oldTask = taskRepository.findById(taskId).orElse(null);
        if (oldTask == null
                || !Objects.equals(oldTask.getStatus().getId(), sprintId)
                || !oldTask.getStatus().getBoard().getSlug().equals(slug)) {
            return null;
        }
        Comment comment = new Comment();
        comment.setText(text);
        comment.setTask(oldTask);
        comment.setCreator(taskCreator);
        return commentRepository.save(comment);
    }
}
