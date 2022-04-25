package com.conjureimage.mtask.repository;

import com.conjureimage.mtask.domain.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
}
