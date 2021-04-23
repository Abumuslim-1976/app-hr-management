package uz.pdp.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.apphrmanagement.entity.Role;
import uz.pdp.apphrmanagement.entity.User;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndEmailCode(@Email String email, String emailCode);


//    List<User> findAllByRoles(Set<Role> roles);

}
