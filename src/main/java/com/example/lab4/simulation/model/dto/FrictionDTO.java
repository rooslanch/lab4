package com.example.lab4.simulation.model.dto;

import com.example.lab4.simulation.model.service.FrictionService;

import java.util.List;

public class FrictionDTO {
    private final List<FrictionSectionDTO> sections;

    public FrictionDTO(List<FrictionSectionDTO> sections) {
        this.sections = List.copyOf(sections); // иммутабельность
    }


    public List<FrictionSectionDTO> getSections() {
        return sections;
    }
    public double getFrictionAt(double x) {
        return FrictionService.getFrictionAt(this, x);
    }
}
