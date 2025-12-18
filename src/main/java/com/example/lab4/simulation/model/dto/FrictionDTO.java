package com.example.lab4.simulation.model.dto;

import java.util.List;

public class FrictionDTO {

    private final List<FrictionSectionDTO> sections;

    public FrictionDTO(List<FrictionSectionDTO> sections) {
        this.sections = List.copyOf(sections); // иммутабельность
    }

    public List<FrictionSectionDTO> getSections() {
        return sections;
    }
}
