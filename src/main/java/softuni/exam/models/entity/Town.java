package softuni.exam.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Town extends BaseEntity{

    @Size(min = 2)
    @Column(unique = true, nullable = false)
    private String townName;

    @Min(0)
    @Column(nullable = false)
    private int population;
}
