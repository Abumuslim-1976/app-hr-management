package uz.pdp.apphrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.SalaryDto;
import uz.pdp.apphrmanagement.service.EmployeeService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;


    //    -------------- Director va Manager uchun xodimlar ro`yxati ko`rinib turadi ------------
    @GetMapping("/showAllEmployee")
    public HttpEntity<?> allEmployee() {
        ApiResponse apiResponse = employeeService.showEmployeeForDirectorAndManager();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    //    ---------- Xodimning belgilangan vaqt bo`yicha ishga kelib ketishini ko`rish -----------
    @GetMapping("/showEmployeeByTime/{id}")
    public HttpEntity<?> employeeByTime(@PathVariable UUID id) {
        ApiResponse apiResponse = employeeService.showOneEmployeeByTurniket(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    //    ----------- Xodimning bajargan tasklari ------------
    @GetMapping("/showEmployeeDoTheTask/{id}")
    public HttpEntity<?> employeeDoTheTask(@PathVariable UUID id) {
        ApiResponse apiResponse = employeeService.showOneEmployeeDoTheTask(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    //    -------- xodimga maosh biriktirish -------
    @PostMapping("/createSalary")
    public HttpEntity<?> createSalary(@RequestBody SalaryDto salaryDto) {
        ApiResponse salaryForEmployee = employeeService.createSalaryForEmployee(salaryDto);
        return ResponseEntity.status(salaryForEmployee.isSuccess() ? 201 : 409).body(salaryForEmployee);
    }


    //    -------- oy bo`yicha ish haqqini ko`rish ---------
    @GetMapping("/salaryByMonth/{id}")
    public HttpEntity<?> salaryByMonth(@PathVariable Integer id) {
        ApiResponse salaryByMonth = employeeService.getSalaryByMonth(id);
        return ResponseEntity.status(salaryByMonth.isSuccess() ? 200 : 409).body(salaryByMonth);
    }


    //    --------- user bo`yicha ish haqqini ko`rish ---------
    @GetMapping("/salaryByUser/{id}")
    public HttpEntity<?> salaryByUser(@PathVariable UUID id) {
        ApiResponse salaryByUser = employeeService.getSalaryByUser(id);
        return ResponseEntity.status(salaryByUser.isSuccess() ? 200 : 409).body(salaryByUser);
    }


    //    --------- rahbariyat vazifalarni o`z vaqtida bajargan yoki bajarmaganini ko`rib turishi --------
    @GetMapping("/taskByTime")
    public HttpEntity<?> taskByTime() {
        ApiResponse taskByTime = employeeService.getTaskByTime();
        return ResponseEntity.status(taskByTime.isSuccess() ? 200 : 409).body(taskByTime);
    }


    /*
     * vaxtida tugatilgan va vaxtida tugatilmagan tasklar haqida
     *
     *  */
    @GetMapping("/byTime")
    public HttpEntity<?> byTime(@RequestParam UUID id, @RequestParam Timestamp byTime) {
        ApiResponse taskOnTime = employeeService.getTaskOnTime(id, byTime);
        return ResponseEntity.status(taskOnTime.isSuccess() ? 200 : 409).body(taskOnTime);
    }

}
