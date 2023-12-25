package softuni.exam.service.impl;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.OfferDtos.OfferDto;
import softuni.exam.models.dto.OfferDtos.OfferWrapperDto;
import softuni.exam.models.entity.Offer;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@AllArgsConstructor
@Service
public class OfferServiceImpl implements OfferService {

    Gson gson;
    ModelMapper modelMapper;
    ApartmentRepository apartmentRepository;
    ValidationUtil validationUtil;
    OfferRepository offerRepository;
    AgentRepository agentRepository;
    XmlParser xmlParser;

    @Override
    public boolean areImported() {
        return offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/xml/offers.xml"));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
               OfferWrapperDto offerWrappers = xmlParser.fromFile(
                       Path.of("src/main/resources/files/xml/offers.xml").toFile(),
                       OfferWrapperDto.class);
          List<OfferDto> offerDtos = offerWrappers.getOfferDtoList();

          List<Offer> offers = offerDtos.stream().filter(offerDto -> {
              boolean isValid = validationUtil.isValid(offerDto);
              if (isValid){
                  if (agentRepository.findByFirstName(offerDto.getAgent().getName()).isPresent()){

                      sb.append(String.format("Successfully imported offer %.2f%n"
                              , offerDto.getPrice()));

                      Offer offerToSave = modelMapper.map(offerDto, Offer.class);
                      offerToSave.setAgent(agentRepository.
                              findByFirstName(offerDto.getAgent().getName()).orElseThrow());

                      offerToSave.setApartment(apartmentRepository.
                             findFirstById(offerDto.getApartment().getId()));

                      offerToSave.setPublishedOn(
                              LocalDate.parse(offerDto.getPublishedOn()
                                      , DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                      offerRepository.saveAndFlush(offerToSave);
                  }else {
                    sb.append("Invalid offer").append(System.lineSeparator());
                  }
              }else {
                  sb.append("Invalid offer").append(System.lineSeparator());
              }

              return isValid;
          }).map(offerDto -> modelMapper.map(offerDto, Offer.class)).collect(Collectors.toList());


        return sb.toString();
    }

    @Override
    public String exportOffers() {
        return null;
    }
}
