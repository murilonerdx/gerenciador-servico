package com.murilonerdx.gerenciador.repository;

import com.murilonerdx.gerenciador.entity.Schedule;
import com.murilonerdx.gerenciador.entity.User;
import com.murilonerdx.gerenciador.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByScheduleAndUser(Schedule schedule, User user);
    List<Vote> findBySchedule(Schedule schedule);
    Optional<Vote> findByUser(User user);
}
