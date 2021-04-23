package uz.pdp.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apphrmanagement.entity.Salary;
import uz.pdp.apphrmanagement.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface SalaryRepository extends JpaRepository<Salary,Integer> {

    Optional<Salary> findByMonthNumber(Integer monthNumber);

    Optional<Salary> findByEmployeeId(UUID employee_id);
}
