package com.murilonerdx.gerenciador.repository;

import com.murilonerdx.gerenciador.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Schedule findByName(String name);
}
