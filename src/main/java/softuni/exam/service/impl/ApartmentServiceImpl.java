package softuni.exam.service.impl;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ApartmentDto;
import softuni.exam.models.dto.ApartmentWrapperDto;
import softuni.exam.models.entity.Apartment;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.ApartmentService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@Service
public class ApartmentServiceImpl implements ApartmentService {

    Gson gson;
    ModelMapper modelMapper;
    ApartmentRepository apartmentRepository;
    ValidationUtil validationUtil;
    TownRepository townRepository;
    XmlParser xmlParser;

    @Override
    public boolean areImported() {
        return apartmentRepository.count() > 0;
    }

    @Override
    public String readApartmentsFromFile() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/xml/apartments.xml"));
    }

    @Override
    public String importApartments() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        ApartmentWrapperDto apartmentWrappers = xmlParser.fromFile(
                Path.of("src/main/resources/files/xml/apartments.xml").toFile()
                , ApartmentWrapperDto.class);

        List <ApartmentDto> apartmentDtos = apartmentWrappers.getApartmentDtos();

        List<Apartment> collection = apartmentDtos.stream().filter(apartmentDto -> {
            boolean isValid = validationUtil.isValid(apartmentDto);

            if (isValid) {
                if (townRepository.findByTownName(apartmentDto.getTownName()).isPresent()
                && !apartmentRepository.findAreaByTown
                        (townRepository.findByTownName(apartmentDto.getTownName())).isPresent()){

                    sb.append(String.format("Successfully imported apartment %s - %.2f%n",
                            apartmentDto.getApartmentType(), apartmentDto.getArea()));

                    Apartment apartmentToSave = modelMapper.map(apartmentDto, Apartment.class);
                    apartmentToSave.setTown
                            (townRepository.findByTownName(apartmentDto.getTownName()).orElseThrow());
                    apartmentRepository.saveAndFlush(apartmentToSave);
                }else {
                    sb.append("Invalid apartment").append(System.lineSeparator());
                }
            } else {
                sb.append("Invalid apartment").append(System.lineSeparator());
            }

            return isValid;
        }).map(a -> modelMapper.map(a, Apartment.class)).collect(Collectors.toList());


        return sb.toString();
    }
}
