package uz.pdp.apphrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import uz.pdp.apphrmanagement.entity.enums.RoleName;
import uz.pdp.apphrmanagement.entity.enums.TaskSituation;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;                    // vazifa nomi

    private String description;             // vazifaning tana qismi

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;            // vazifa tayinlangan vaqt

    private Timestamp expirationTime;       // vazifa yakunlanishi kerak bo`lgan vaqt

    @ManyToOne
    private User responsible;               // vazifaga mas`ul shaxs

    @ManyToOne
    private User taskLoadToUser;            // vazifa biriktiradigan shaxs

    @Enumerated(EnumType.STRING)
    private TaskSituation taskSituation;    // vazifa holati

}
