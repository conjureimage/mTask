package com.conjureimage.mtask.repository;

import com.conjureimage.mtask.domain.Status;
import com.conjureimage.mtask.domain.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findByStatus(Status status);

    List<Task> findByStatusOrderByPosition(Status status);

    Optional<Task> findById(Long id);
}
