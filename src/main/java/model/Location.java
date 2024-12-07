package model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
    @NotNull(message = "X cannot be null")
    private Double x; // Поле не может быть null

    @NotNull(message = "Y cannot be null")
    private Integer y; // Поле не может быть null

    @NotNull(message = "Z cannot be null")
    private Integer z; // Поле не может быть null

    @NotNull(message = "Name cannot be null")
    @NotEmpty(message = "Name cannot be empty")
    private String name; // Строка не может быть пустой, Поле не может быть null
}
