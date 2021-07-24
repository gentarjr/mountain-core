package com.mountain.repo;

import com.mountain.entity.detail.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRepo extends JpaRepository<App, Long> {
    App findByName(App.AppCode nameApp);
}
