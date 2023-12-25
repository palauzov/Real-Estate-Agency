package softuni.exam.service.impl;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.AgentDto;
import softuni.exam.models.entity.Agent;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.AgentService;
import softuni.exam.util.ValidationUtil;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AgentServiceImpl implements AgentService {
    Gson gson;
    ModelMapper modelMapper;
    AgentRepository agentRepository;
    ValidationUtil validationUtil;
    TownRepository townRepository;


    @Override
    public boolean areImported() {
        return agentRepository.count() > 0;
    }

    @Override
    public String readAgentsFromFile() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/json/agents.json"));
    }

    @Override
    public String importAgents() throws IOException {
        StringBuilder sb = new StringBuilder();
        AgentDto[] agentDtos = gson.fromJson(readAgentsFromFile(), AgentDto[].class);

        List<Agent> agents = Arrays.stream(agentDtos).filter(agentDto -> {
            boolean isValid = validationUtil.isValid(agentDto);
            if (agentRepository.findByFirstName(agentDto.getFirstName()).isPresent()){
                 isValid = false;
            }
            if (isValid){
                if (townRepository.findByTownName(agentDto.getTown()).isPresent()){
                    sb.append(String.format("Successfully imported agent - %s %s%n",
                            agentDto.getFirstName(),
                            agentDto.getLastName()));

                   Agent agentToSave = modelMapper.map(agentDto, Agent.class);
                   agentToSave.setTown(townRepository.findByTownName(agentDto.getTown())
                           .orElseThrow());
                   agentRepository.saveAndFlush(agentToSave);
                }else {
                    sb.append("Invalid agent").append(System.lineSeparator());
                }
            }else {
                sb.append("Invalid agent").append(System.lineSeparator());
            }

            return isValid;
        }).map(a -> modelMapper.map(a, Agent.class)).collect(Collectors.toList());

        return sb.toString();
    }
}
