package com.example.lab4.simulation.model.mapper;

import com.example.lab4.simulation.model.FrictionProfile;
import com.example.lab4.simulation.model.dto.FrictionDTO;
import com.example.lab4.simulation.model.dto.FrictionSectionDTO;

import java.util.List;
import java.util.stream.Collectors;

public class FrictionMapper {

    public static FrictionDTO toDTO(FrictionProfile profile) {
        List<FrictionSectionDTO> sections =
                profile.getSections().entrySet().stream()
                        .map(e -> new FrictionSectionDTO(e.getKey(), e.getValue()))
                        .collect(Collectors.toList());

        return new FrictionDTO(sections);
    }
}
