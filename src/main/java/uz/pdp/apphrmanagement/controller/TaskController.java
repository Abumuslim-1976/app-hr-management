package uz.pdp.apphrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphrmanagement.entity.Task;
import uz.pdp.apphrmanagement.entity.User;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.TaskDto;
import uz.pdp.apphrmanagement.service.TaskService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    TaskService taskService;


//    Director yoki manager o`z xodimiga vazifa biriktirishi
    @PostMapping("/attachTheTask")
    public HttpEntity<?> createTask(@Valid @RequestBody TaskDto taskDto) {
        ApiResponse apiResponse = taskService.attachTaskToManagerAndWorker(taskDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }


//    vazifa bajarilganligi haqida boshlig`ining emailiga xabar jo`natadi
    @PutMapping("/completedTheTask/{id}")
    public HttpEntity<?> completedTask(@Valid @PathVariable Integer id, @RequestBody TaskDto taskDto) {
        ApiResponse apiResponse = taskService.employeeDoTheTask(id, taskDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }


//    Director yoki Manager xodimiga vazifa biriktirib emailiga link jo`natganda xodim vazifani holatini PROCCESS(jarayonda) qilishi
    @GetMapping("/attachTheTask")
    public HttpEntity<?> taskProcess(@RequestParam String email, @RequestParam Integer id) {
        ApiResponse apiResponse = taskService.doTheTask(email, id);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
