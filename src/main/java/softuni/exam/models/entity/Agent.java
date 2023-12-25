package softuni.exam.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Agent extends BaseEntity{

    @Size(min = 2)
    @Column(unique = true, nullable = false)
    private String firstName;

    @Size(min = 2)
    @Column(nullable = false)
    private String lastName;

    @Email
    @Column(nullable = false)
    private String email;

    @ManyToOne
    private Town town;
}
