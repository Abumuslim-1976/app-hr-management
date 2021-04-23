package uz.pdp.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.apphrmanagement.entity.Task;
import uz.pdp.apphrmanagement.entity.User;

import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    Optional<Task> findByResponsibleId(UUID responsible_id);

    Optional<Task> findByIdAndResponsibleEmail(Integer id, @Email String responsible_email);

    @Query(nativeQuery = true, value = "select * from task where task.expirationTime <=: byTime")
    List<Task> findTaskByOnTime(LocalDateTime byTime);
}
