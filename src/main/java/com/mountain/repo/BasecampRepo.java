package com.mountain.repo;

import com.mountain.entity.detail.Basecamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasecampRepo extends JpaRepository<Basecamp, String> {
}
