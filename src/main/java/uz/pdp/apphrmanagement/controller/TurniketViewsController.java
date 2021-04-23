package uz.pdp.apphrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.TurniketViewsDto;
import uz.pdp.apphrmanagement.service.TurniketViewsService;

@RestController
@RequestMapping("/api/turniketViews")
public class TurniketViewsController {

    @Autowired
    TurniketViewsService turniketViewsService;


    //    ---------- xodimning ishga kirish va chiqish vaqti ----------
    @PostMapping
    public HttpEntity<?> enterOrExitTime(@RequestBody TurniketViewsDto turniketViewsDto) {
        ApiResponse apiResponse = turniketViewsService.accessOrExitTime(turniketViewsDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

}
