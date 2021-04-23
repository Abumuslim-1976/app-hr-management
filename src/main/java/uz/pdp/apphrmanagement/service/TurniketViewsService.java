package uz.pdp.apphrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagement.entity.Role;
import uz.pdp.apphrmanagement.entity.Turniket;
import uz.pdp.apphrmanagement.entity.TurniketViews;
import uz.pdp.apphrmanagement.entity.User;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.TurniketViewsDto;
import uz.pdp.apphrmanagement.repository.TurniketRepository;
import uz.pdp.apphrmanagement.repository.TurniketViewsRepo;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
public class TurniketViewsService {

    @Autowired
    TurniketViewsRepo turniketViewsRepo;
    @Autowired
    TurniketRepository turniketRepository;

    //    --------- xodimning ishga kirish va chiqish vaqti ----------
    public ApiResponse accessOrExitTime(TurniketViewsDto turniketViewsDto) {
        Optional<Turniket> optionalTurniket = turniketRepository.findById(turniketViewsDto.getTurniketId());
        if (!optionalTurniket.isPresent())
            return new ApiResponse("Turniket not found", false);

        TurniketViews turniketViews = new TurniketViews();
        turniketViews.setTurniket(optionalTurniket.get());

        if (turniketViewsDto.isAccessAndExitMode()) {
            turniketViews.setAccessOrExitTime(LocalDateTime.now());
            turniketViews.setAccessAndExitMode(true);
            turniketViewsRepo.save(turniketViews);
            return new ApiResponse("User logged in " + turniketViews.getAccessOrExitTime(), true);
        } else {
            turniketViews.setAccessOrExitTime(LocalDateTime.now());
            turniketViews.setAccessAndExitMode(false);
            turniketViewsRepo.save(turniketViews);
            return new ApiResponse("User left the system " + turniketViews.getAccessOrExitTime(), true);
        }
    }

}
