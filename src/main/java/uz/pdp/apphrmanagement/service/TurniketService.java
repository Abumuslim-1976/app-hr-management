package uz.pdp.apphrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagement.entity.Role;
import uz.pdp.apphrmanagement.entity.Turniket;
import uz.pdp.apphrmanagement.entity.User;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.TurniketDto;
import uz.pdp.apphrmanagement.repository.TurniketRepository;
import uz.pdp.apphrmanagement.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class TurniketService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TurniketRepository turniketRepository;


    //    ------- ishga kirish vaqtini yozib borish uchun -------
    public ApiResponse accessToWork() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.getRoleName().name().equals("DIRECTOR") || role.getRoleName().name().equals("HR_MANAGER")) {
                return new ApiResponse("only employees are registered here", false);
            }
        }

        Optional<User> userOptional = userRepository.findById(user.getId());
        if (!userOptional.isPresent())
            return new ApiResponse("User not found", false);

        Turniket turniket = new Turniket();
        turniket.setUser(userOptional.get());
        turniket.setAccessAndExitMode(true);
        turniket.setAccessTime(LocalDateTime.now());
        turniketRepository.save(turniket);
        return new ApiResponse("Success , welcome !!!", true);

    }


    //    ------- ishdan chiqish vaqtini yozib borish uchun -------
    public ApiResponse exitToWork() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Turniket> turniketOptional = turniketRepository.findByUserId(user.getId());
        if (!turniketOptional.isPresent())
            return new ApiResponse("User not found from turniket", false);

        Turniket turniket = turniketOptional.get();
        turniket.setExitTime(LocalDateTime.now());
        turniket.setAccessAndExitMode(false);
        turniketRepository.save(turniket);
        return new ApiResponse("see you !!!", true);

    }


//    ----------- Turniketlar tarixi -----------
    public List<Turniket> getAllTurniket() {
        return turniketRepository.findAll();
    }

}
