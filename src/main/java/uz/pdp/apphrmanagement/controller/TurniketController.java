package uz.pdp.apphrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphrmanagement.entity.Turniket;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.TurniketDto;
import uz.pdp.apphrmanagement.service.TurniketService;

import java.util.List;

@RestController
@RequestMapping("/api/turniket")
public class TurniketController {

    @Autowired
    TurniketService turniketService;


    //    ------- ishga kirish vaqtini yozib borish uchun -------
    @PostMapping
    public HttpEntity<?> access() {
        ApiResponse apiResponse = turniketService.accessToWork();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    //    ------- ishdan chiqish vaqtini yozib borish uchun -------
    @PutMapping
    public HttpEntity<?> exit() {
        ApiResponse apiResponse = turniketService.exitToWork();
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }


    //    ----------- Turniketlar tarixi -----------
    @GetMapping
    public HttpEntity<List<Turniket>> getAll() {
        List<Turniket> allTurniket = turniketService.getAllTurniket();
        return ResponseEntity.ok(allTurniket);
    }

}
