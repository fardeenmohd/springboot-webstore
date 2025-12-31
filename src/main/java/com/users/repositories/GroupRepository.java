package com.users.repositories;

import com.users.model.Group;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface GroupRepository extends JpaRepositoryImplementation<Group, Long> {
}
