package com.users.repositories;

import com.users.model.Profile;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface ProfileRepository extends JpaRepositoryImplementation<Profile, Long> {
}
