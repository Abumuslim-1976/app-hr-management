package uz.pdp.apphrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagement.entity.Role;
import uz.pdp.apphrmanagement.entity.Task;
import uz.pdp.apphrmanagement.entity.User;
import uz.pdp.apphrmanagement.entity.enums.RoleName;
import uz.pdp.apphrmanagement.entity.enums.TaskSituation;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.LoginDto;
import uz.pdp.apphrmanagement.payload.RegisterDto;
import uz.pdp.apphrmanagement.repository.RoleRepository;
import uz.pdp.apphrmanagement.repository.TaskRepository;
import uz.pdp.apphrmanagement.repository.UserRepository;
import uz.pdp.apphrmanagement.security.JwtProvider;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    TaskRepository taskRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + " topilmadi"));
    }


//    ---------- user registratsiyadan o`tishi ----------
    public ApiResponse registerToSystem(RegisterDto registerDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.getRoleName().name().equals("DIRECTOR")) {
                User manager = new User();
                manager.setFirstName(registerDto.getFirstName());
                manager.setLastName(registerDto.getLastName());
                manager.setEmail(registerDto.getEmail());
                manager.setPassword(passwordEncoder.encode(registerDto.getPassword()));
                manager.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.HR_MANAGER)));
                manager.setEmailCode(UUID.randomUUID().toString());
                userRepository.save(manager);

                sendSimpleMessage(manager.getEmail(), manager.getEmailCode());
                return new ApiResponse("Manager save", true);
            }
        }


        for (Role role : roles) {
            if (role.getRoleName().name().equals("HR_MANAGER")) {
                User worker = new User();
                worker.setFirstName(registerDto.getFirstName());
                worker.setLastName(registerDto.getLastName());
                worker.setEmail(registerDto.getEmail());
                worker.setPassword(passwordEncoder.encode(registerDto.getPassword()));
                worker.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.WORKER)));
                worker.setEmailCode(UUID.randomUUID().toString());

                userRepository.save(worker);
                sendSimpleMessage(worker.getEmail(), worker.getEmailCode());
                return new ApiResponse("worker save", true);
            }
        }

        return new ApiResponse("worker can not register", false);
    }


//    ---------- user registratsiyadan o`tgandan keyin login qilishi -----------
    public ApiResponse loginToSystem(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(),
                    loginDto.getPassword()
            ));
            User user = (User) authentication.getPrincipal();
            String token = jwtProvider.generateToken(loginDto.getUsername(), user.getRoles());
            return new ApiResponse("User login to system", true, token);
        } catch (BadCredentialsException e) {
            return new ApiResponse("email or login not found", false);
        }

    }


//    ---------- userni emailiga link jo`natish -----------
    public void sendSimpleMessage(String email, String emailCode) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("noreply@gmail.com");
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("Akkountni tasdiqlash");
            simpleMailMessage.setText("<a href='http://localhost:8080/api/auth/verifyEmail?emailCode=" + emailCode + "&email=" + email + "'>Tasdiqlash</a>");
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


//    ---------- akkountni tasdiqlash -----------
    public ApiResponse verifyEmail(String email, String emailCode) {
        Optional<User> optionalUser = userRepository.findByEmailAndEmailCode(email, emailCode);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(true);
            user.setPassword(passwordEncoder.encode("qwerty123"));
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("Akkount tasdiqlandi", true);
        }
        return new ApiResponse("Akkount allaqachon tasdiqlangan", false);
    }
}
