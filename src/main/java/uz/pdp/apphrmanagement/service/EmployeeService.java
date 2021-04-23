package uz.pdp.apphrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagement.entity.*;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.SalaryDto;
import uz.pdp.apphrmanagement.repository.SalaryRepository;
import uz.pdp.apphrmanagement.repository.TaskRepository;
import uz.pdp.apphrmanagement.repository.TurniketRepository;
import uz.pdp.apphrmanagement.repository.UserRepository;

import java.security.Security;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class EmployeeService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TurniketRepository turniketRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    SalaryRepository salaryRepository;


    //    ------------ Director va Manager uchun xodimlar ro`yxati ko`rinib turadi ------------
    public ApiResponse showEmployeeForDirectorAndManager() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.getRoleName().name().equals("DIRECTOR") || role.getRoleName().name().equals("HR_MANAGER")) {
                List<User> usersList = userRepository.findAll();
                return new ApiResponse("Followings are the list of all employees: ", true, usersList);
            }
        }
        return new ApiResponse("Cannot get the list!", false);
    }


    //    ---------- Xodimning belgilangan vaqt bo`yicha ishga kelib ketishini ko`rish -----------
    public ApiResponse showOneEmployeeByTurniket(UUID id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.getRoleName().name().equals("MANAGER") || role.getRoleName().name().equals("WORKER")) {
                return new ApiResponse("Only director and hr manager can see this list", false);
            }
        }

        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent())
            return new ApiResponse("User with this id is not found!", false);

        Optional<Turniket> turniketOptional = turniketRepository.findByUserId(id);
        if (!turniketOptional.isPresent()) {
            return new ApiResponse("No information about user attendance found!", false);
        }

        Turniket turniket = turniketOptional.get();
        return new ApiResponse("Followings are the attendance rate of the chosen user: ", true, turniket);

    }


    //    ----------- Xodimning bajargan tasklari ------------
    public ApiResponse showOneEmployeeDoTheTask(UUID id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.getRoleName().name().equals("DIRECTOR")) {
                return new ApiResponse("the task is not assigned to the director !!!", false);
            }
        }

        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent())
            return new ApiResponse("User not found", false);

        Optional<Task> optionalTask = taskRepository.findByResponsibleId(id);
        if (!optionalTask.isPresent())
            return new ApiResponse("Task not found", false);

        Task task = optionalTask.get();
        return new ApiResponse("task performed by a single worker", true, task);
    }


    //    -------- xodimga maosh biriktirish -------
    public ApiResponse createSalaryForEmployee(SalaryDto salaryDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.getRoleName().name().equals("DIRECTOR") || role.getRoleName().name().equals("HR_MANAGER")) {

                Optional<User> userOptional = userRepository.findById(salaryDto.getEmployeeId());
                if (!userOptional.isPresent())
                    return new ApiResponse("User not found", false);

                User employee = userOptional.get();
                Set<Role> employeeRoles = employee.getRoles();
                for (Role employeeRole : employeeRoles) {
                    if (employeeRole.getRoleName().name().equals("WORKER")) {
                        Salary salary = new Salary();
                        salary.setMonthNumber(salaryDto.getMonthNumber());
                        salary.setMonthlySalary(salaryDto.getMonthlyAmount());
                        salary.setEmployee(employee);
                        salaryRepository.save(salary);
                        return new ApiResponse(employee.getFirstName() + " " + employee.getLastName()
                                + " to was paid " + salary.getMonthlySalary() + " soums", true);
                    } else {
                        return new ApiResponse("The salaries here are for employees only", false);
                    }
                }

            } else {
                return new ApiResponse("The salary is paid only by the director and manager", false);
            }
        }
        return new ApiResponse("there is no such role", false);
    }


    //    -------- oy bo`yicha ish haqqini ko`rish ---------
    public ApiResponse getSalaryByMonth(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (!role.getRoleName().name().equals("DIRECTOR")) {
                return new ApiResponse("Only director can see the salaries", false);
            }
        }

        Optional<Salary> byMonthNumber = salaryRepository.findByMonthNumber(id);
        if (!byMonthNumber.isPresent())
            return new ApiResponse("Month not found from salary warehouse", false);

        Salary salary = byMonthNumber.get();
        return new ApiResponse("get salary by month", true, salary);
    }


    //    --------- user bo`yicha ish haqqini ko`rish ---------
    public ApiResponse getSalaryByUser(UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (!role.getRoleName().name().equals("DIRECTOR")) {
                return new ApiResponse("Only director can see the salaries", false);
            }
        }

        Optional<Salary> byEmployeeId = salaryRepository.findByEmployeeId(id);
        if (!byEmployeeId.isPresent())
            return new ApiResponse("user not found from salary warehouse", false);

        Salary salary = byEmployeeId.get();
        return new ApiResponse("get salary by user", true, salary);
    }


    //    --------- rahbariyat vazifalarni o`z vaqtida bajargan yoki bajarmaganini ko`rib turishi --------
    public ApiResponse getTaskByTime() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.getRoleName().name().equals("WORKER")) {
                return new ApiResponse("director,hr_manager can see the salaries", false);
            }
        }

        List<Task> taskList = taskRepository.findAll();
        return new ApiResponse("Task list", true, taskList);
    }


}
