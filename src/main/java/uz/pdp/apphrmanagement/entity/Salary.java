package uz.pdp.apphrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Salary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer monthNumber;               // maosh beriladigan oy

    @Column(nullable = false)
    private Double monthlySalary;       // maosh

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;        // maosh berilgan vaqt

    private boolean isPaid;             // maosh to`langan yoki to`lanmaganligi

    @OneToOne
    private User employee;              // maosh olgan xodim

}
