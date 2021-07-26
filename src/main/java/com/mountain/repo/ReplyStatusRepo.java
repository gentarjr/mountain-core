package com.mountain.repo;

import com.mountain.entity.user.ReplyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyStatusRepo extends JpaRepository<ReplyStatus, String> {
}
