package uz.pdp.apphrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TurniketViews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(updatable = false)
    private LocalDateTime accessOrExitTime;              // xodimning kirish yoki chiqish vaqti

    private boolean accessAndExitMode;                   // xodimning ayni paytdagi kirish yoki chiqish holati

    @ManyToOne
    private Turniket turniket;
}
