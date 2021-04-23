package uz.pdp.apphrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Turniket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(updatable = false)
    private LocalDateTime accessTime;              // xodimning kirish vaqti

    @Column(updatable = false)
    private LocalDateTime exitTime;                // xodimning chiqish vaqti

    private boolean accessAndExitMode;             // xodimning ayni paytdagi kirish yoki chiqish holati

    @OneToOne
    private User user;                             // turniketdan o`tuvchi xodim

}
