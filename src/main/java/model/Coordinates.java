package model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coordinates {
    @Min(value = -371, message = "X should be greater than -371")
    private double x; // Значение поля должно быть больше -371

    @Min(value = 1, message = "Y should be greater than 0")
    private long y; // Значение поля должно быть больше 0
}
