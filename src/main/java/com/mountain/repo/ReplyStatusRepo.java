package com.mountain.repo;

import com.mountain.entity.user.ReplyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyStatusRepo extends JpaRepository<ReplyStatus, String> {
    ReplyStatus findByStatusId(String id);

    List<ReplyStatus> findByStatusIdOrderByCreatedDateAsc(String id);
}
