package com.kooz.giggy.repository;

import com.kooz.giggy.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository  extends JpaRepository<Post, Long> {
    List<Post> findAllByUserId(Long userId, Pageable pageable);
}
