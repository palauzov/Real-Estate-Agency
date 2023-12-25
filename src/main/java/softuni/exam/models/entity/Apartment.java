package softuni.exam.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table

public class Apartment extends BaseEntity{

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type apartmentType;

    @DecimalMin("40.00")
    @Column(nullable = false)
    private double area;

    @ManyToOne
    private Town town;
}
