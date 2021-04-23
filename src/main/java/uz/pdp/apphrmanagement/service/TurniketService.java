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

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class TurniketService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TurniketRepository turniketRepository;


    //    --------- xodimlarga turniket beriladi ----------
    public ApiResponse addTurniketToUser(TurniketDto turniketDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.getRoleName().name().equals("DIRECTOR") || role.getRoleName().name().equals("HR_MANAGER")) {

                Optional<User> userOptional = userRepository.findById(turniketDto.getUserId());
                if (!userOptional.isPresent())
                    return new ApiResponse("This user not found", false);

                boolean exists = turniketRepository.existsByUserId(turniketDto.getUserId());
                if (exists)
                    return new ApiResponse("this user has already been added", false);

                Turniket turniket = new Turniket();
                turniket.setUser(userOptional.get());
                turniketRepository.save(turniket);
                return new ApiResponse("user join the turniket", true, userOptional.get());
            } else {
                return new ApiResponse("user cannot join the turniket , because only director or hr_manager can add turniket", false);
            }
        }

        return new ApiResponse("ERROR", false);
    }


    //    --------- turniketlar tarixi -----------
    public ApiResponse getAllTurniket() {
        List<Turniket> turniketList = turniketRepository.findAll();
        return new ApiResponse("Get all turniket list", true, turniketList);
    }


}
