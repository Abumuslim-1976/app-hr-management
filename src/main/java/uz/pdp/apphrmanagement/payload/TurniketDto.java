package uz.pdp.apphrmanagement.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.apphrmanagement.entity.User;

import javax.persistence.OneToOne;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TurniketDto {

    private LocalDateTime accessTime;               // xodimning kirish vaqti

    private LocalDateTime exitTime;                 // xodimning chiqish vaqti

    private boolean accessAndExitMode;              // xodimning ayni paytdagi kirish yoki chiqish holati

    private UUID userId;                            // turniketdan o`tuvchi xodim

}
