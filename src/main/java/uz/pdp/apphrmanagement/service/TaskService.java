package uz.pdp.apphrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagement.entity.Role;
import uz.pdp.apphrmanagement.entity.Task;
import uz.pdp.apphrmanagement.entity.User;
import uz.pdp.apphrmanagement.entity.enums.RoleName;
import uz.pdp.apphrmanagement.entity.enums.TaskSituation;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.TaskDto;
import uz.pdp.apphrmanagement.repository.TaskRepository;
import uz.pdp.apphrmanagement.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthService authService;


//         ------ Boshliq o`zining xodimlariga vazifa biriktirishi va emailiga link jo`natishi ------
    public ApiResponse attachTaskToManagerAndWorker(TaskDto taskDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<Role> roles = user.getRoles();

        for (Role role : roles) {
            if (role.getRoleName().name().equals("DIRECTOR")) {
                Optional<User> userOptional = userRepository.findById(taskDto.getResponsibleId());
                if (!userOptional.isPresent())
                    return new ApiResponse("Responsible User not found", false);

                User responsible = userOptional.get();
                responsible.setEmailCode(UUID.randomUUID().toString());

                Set<Role> roles1 = responsible.getRoles();
                for (Role role1 : roles1) {
                    if (role1.getRoleName().name().equals("HR_MANAGER") || role1.getRoleName().name().equals("WORKER")) {
                        Task task = new Task();
                        task.setName(taskDto.getName());
                        task.setDescription(taskDto.getDescription());
                        task.setExpirationTime(taskDto.getExpirationTime());
                        task.setTaskSituation(TaskSituation.NEW);
                        task.setResponsible(responsible);
                        task.setTaskLoadToUser(user);
                        taskRepository.save(task);

                        sendTaskToWorker(responsible.getEmail(), responsible.getEmailCode());
                        return new ApiResponse("task assigned", true);
                    } else {
                        return new ApiResponse("sorry !!! , You can only assign a task to Worker and Manager", false);
                    }
                }

            }
        }


        for (Role role : roles) {
            if (role.getRoleName().name().equals("HR_MANAGER")) {
                Optional<User> userOptional = userRepository.findById(taskDto.getResponsibleId());
                if (!userOptional.isPresent())
                    return new ApiResponse("Responsible User not found", false);

                User responsibleTwo = userOptional.get();
                responsibleTwo.setEmailCode(UUID.randomUUID().toString());

                Set<Role> roleResponsible = responsibleTwo.getRoles();
                for (Role role1 : roleResponsible) {
                    if (role1.getRoleName().name().equals("WORKER")) {
                        Task taskTwo = new Task();
                        taskTwo.setName(taskDto.getName());
                        taskTwo.setDescription(taskDto.getDescription());
                        taskTwo.setExpirationTime(taskDto.getExpirationTime());
                        taskTwo.setTaskSituation(TaskSituation.NEW);
                        taskTwo.setResponsible(responsibleTwo);
                        taskTwo.setTaskLoadToUser(user);
                        taskRepository.save(taskTwo);

                        sendTaskToWorker(responsibleTwo.getEmail(), responsibleTwo.getEmailCode());
                        return new ApiResponse("task assigned", true);
                    } else {
                        return new ApiResponse("sorry !!! , You can only assign a task to Worker", false);
                    }
                }

            }
        }

        return new ApiResponse("This user cannot assign a task", false);
    }



//    -------- xodim vazifasini bajarib boshlig`ini emailiga link jo`natishi --------
    public ApiResponse employeeDoTheTask(Integer id, TaskDto taskDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();

            if (user.getId().equals(task.getResponsible().getId())) {
                task.setTaskSituation(TaskSituation.COMPLETED);
                task.setDescription(taskDto.getDescription());
                taskRepository.save(task);

                sendDoTheTaskToManager(task.getTaskLoadToUser().getEmail(), task.getResponsible().getId());
                return new ApiResponse("Notification was sent that the task had been completed", true);
            }
        }

        return new ApiResponse("Task not found", false);
    }


//     ---------- Boshliq xodimiga vazifa biriktirishi methodi ----------
    public void sendTaskToWorker(String email, String emailCode) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("noreply@gmail.com");
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("Send task to worker");
            simpleMailMessage.setText("<a href='http://localhost:8080/api/auth/attachTheTask?emailCode=" + emailCode + "&email=" + email + "'>Tasdiqlash</a>");
            javaMailSender.send(simpleMailMessage);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


//    ----------- Vazifa bajarib bo`lingani xaqida link jo`natishi methodi -----------
    public void sendDoTheTaskToManager(String email, UUID id) {
        try {
            Optional<User> userOptional = userRepository.findById(id);

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("noreply@gmail.com");
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("Task completed");
            simpleMailMessage.setText("The worker " + userOptional.get().getFirstName() + " " + userOptional.get().getLastName() + " completed the task");
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


//     --------- Xodimni emailiga xabar kelganda uni tasdiqlab , vazifani PROCCESS(jarayonda ekanligini) aytishi -----------
    public ApiResponse doTheTask(String email, Integer id) {
        Optional<Task> taskOptional = taskRepository.findByIdAndResponsibleEmail(id, email);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setTaskSituation(TaskSituation.PROCESS);
            taskRepository.save(task);
            return new ApiResponse("the employee began to perform the task", true);
        }else {
            return new ApiResponse("Task with this user email is not found", false);
        }
    }



}
