package model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import javax.validation.constraints.*;
import java.time.ZonedDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 1, message = "Id should be greater than 0")
    private long id; // Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @NotNull(message = "Name cannot be null")
    @NotEmpty(message = "Name cannot be empty")
    private String name; // Поле не может быть null, Строка не может быть пустой

    @NotNull(message = "Coordinates cannot be null")
    @Embedded
    private Coordinates coordinates; // Поле не может быть null

    @NotNull(message = "Creation date cannot be null")
    private ZonedDateTime creationDate; // Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @NotNull(message = "Height cannot be null")
    @Min(value = 1, message = "Height should be greater than 0")
    private Float height; // Поле не может быть null, Значение поля должно быть больше 0

    @NotNull(message = "Birthday cannot be null")
    private Date birthday; // Поле не может быть null

    @Min(value = 1, message = "Weight should be greater than 0")
    private Float weight; // Поле может быть null, Значение поля должно быть больше 0

    @NotNull(message = "Hair color cannot be null")
    @Enumerated(EnumType.STRING)
    private Color hairColor; // Поле не может быть null

    @NotNull(message = "Location cannot be null")
    @Embedded
    private Location location; // Поле не может быть null
}
