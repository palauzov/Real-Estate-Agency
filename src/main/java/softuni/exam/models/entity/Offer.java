package softuni.exam.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Offer extends BaseEntity{

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private LocalDate publishedOn;

    @ManyToOne
    private Agent agent;

    @ManyToOne
    private Apartment apartment;
}
