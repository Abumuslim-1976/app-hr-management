package uz.pdp.apphrmanagement.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.apphrmanagement.entity.User;
import uz.pdp.apphrmanagement.entity.enums.RoleName;
import uz.pdp.apphrmanagement.repository.RoleRepository;
import uz.pdp.apphrmanagement.repository.UserRepository;

import java.util.Collections;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    @Value(value = "${spring.datasource.initialization-mode}")
    private String initialMode;

    @Override
    public void run(String... args) throws Exception {
        if (initialMode.equals("always")) {
            User user = new User();
            user.setFirstName("Akmal");
            user.setLastName("Boymatov");
            user.setPassword(passwordEncoder.encode("abc123"));
            user.setEmail("akmal2233@gmail.com");
            user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.DIRECTOR)));
            userRepository.save(user);
        }

        if (initialMode.equals("always")) {
            User user = new User();
            user.setFirstName("Aziz");
            user.setLastName("Hakimov");
            user.setPassword(passwordEncoder.encode("ccc222"));
            user.setEmail("aziz4433@gmail.com");
            user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.HR_MANAGER)));
            userRepository.save(user);
        }

    }
}
