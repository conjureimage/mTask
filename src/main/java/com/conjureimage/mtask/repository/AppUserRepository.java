package com.conjureimage.mtask.repository;

import com.conjureimage.mtask.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByEmail(String email);

    @SuppressWarnings("JpaQlInspection")
    @Query("select m from AppUser m where lower(concat(m.username, ' ') ) like lower(concat('%',:query, '%') ) ")
    List<AppUser> findUsers(@Param("query") String query);
}
