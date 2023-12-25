package softuni.exam.models.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class TownDto {

    @Size(min = 2)
    @NotNull
    private String townName;

    @Min(0)
    @NotNull
    private int population;
}
