package uz.pdp.apphrmanagement.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalaryDto {

    // maosh beriladigan oy
    @NotNull(message = "Maosh qaysi oyda berilganligini kiriting")
    private Integer monthNumber;

    // maosh
    @NotNull(message = "Maoshning miqdorini kiriting")
    private Double monthlyAmount;

    // maosh olgan xodim
    @NotNull(message = "Maosh oladigan xodimning ID sini kiriting")
    private UUID employeeId;

    private boolean isPaid;

}
