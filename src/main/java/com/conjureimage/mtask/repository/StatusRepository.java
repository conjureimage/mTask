package com.conjureimage.mtask.repository;

import com.conjureimage.mtask.domain.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StatusRepository extends CrudRepository<Status, Long> {

    Optional<Status> findById(Long id);
}
