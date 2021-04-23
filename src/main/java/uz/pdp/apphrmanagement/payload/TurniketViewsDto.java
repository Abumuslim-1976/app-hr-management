package uz.pdp.apphrmanagement.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TurniketViewsDto {

    private boolean accessAndExitMode;

    private Integer turniketId;

}
