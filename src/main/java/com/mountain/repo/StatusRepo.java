package com.mountain.repo;

import com.mountain.entity.user.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRepo extends JpaRepository<Status, String> {

    List<Status> findByMountainIdOrderByCreatedDateAsc(String mountainId);

    @Query(value = "SELECT a.id as id, a.photo as photo, a.username as username, a.role as role, a.status as status, a.createdDate as created_date" +
            " FROM Status a where a.mountainId is null")
    List<Status> findStatusUser();

    List<Status> findByUsersId(String id);
}
