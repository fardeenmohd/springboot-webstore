package com.users.repositories;

import com.users.model.Post;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface PostRepository extends JpaRepositoryImplementation<Post, Long> {
}
