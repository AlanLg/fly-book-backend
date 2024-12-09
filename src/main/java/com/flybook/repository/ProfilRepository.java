package com.flybook.repository;

import com.flybook.model.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilRepository extends JpaRepository<Profile, Long> {
}
