package softuni.exam.models.dto.OfferDtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.exam.models.dto.OfferDtos.AgentName;
import softuni.exam.models.dto.OfferDtos.ApartmentId;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "offer")
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferDto {

    @NotNull
    @XmlElement
    private double price;

    @XmlElement
    private String publishedOn;

    @XmlElement
    @NotNull
    private AgentName agent;

    @XmlElement
    @NotNull
    private ApartmentId apartment;
}
