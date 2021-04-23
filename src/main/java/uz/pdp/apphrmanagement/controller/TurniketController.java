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


    //    --------- xodimlarga turniket beriladi -----------
    @PostMapping("/addTurniket")
    public HttpEntity<?> addTurniket(@RequestBody TurniketDto turniketDto) {
        ApiResponse apiResponse = turniketService.addTurniketToUser(turniketDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }


    //    -------- turniketlar tarixi ---------
    @GetMapping
    public HttpEntity<?> getAll() {
        ApiResponse allTurniket = turniketService.getAllTurniket();
        return ResponseEntity.ok(allTurniket);
    }

}
