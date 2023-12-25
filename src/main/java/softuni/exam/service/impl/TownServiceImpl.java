package softuni.exam.service.impl;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TownDto;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TownServiceImpl implements TownService {

    TownRepository townRepository;
    Gson gson;
    ModelMapper modelMapper;
    ValidationUtil validationUtil;



    @Override
    public boolean areImported() {
        return townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/json/towns.json"));
    }

    @Override
    public String importTowns() throws IOException {
        StringBuilder sb = new StringBuilder();
        TownDto[] townDtos = gson.fromJson(readTownsFileContent(), TownDto[].class);

       List<Town> towns = Arrays.stream(townDtos).filter(townDto -> {
            boolean isValid = validationUtil.isValid(townDto);

           if (townRepository.findByTownName(townDto.getTownName()).isPresent()){
               isValid = false;
           }

            if (isValid) {

                    sb.append(String.format("Successfully imported town %s - %d%n",
                            townDto.getTownName(), townDto.getPopulation()));
                    Town town = modelMapper.map(townDto, Town.class);
                    townRepository.saveAndFlush(town);


            }else {
                sb.append("Invalid town").append(System.lineSeparator());
            }


           return isValid;
        }).map(t -> modelMapper.map(t, Town.class)).collect(Collectors.toList());


        return sb.toString();
    }
}
