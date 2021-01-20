package com.getterz.domain.repository;

import com.getterz.domain.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {
    Optional<Tag> findFirstByName(String name);
}
