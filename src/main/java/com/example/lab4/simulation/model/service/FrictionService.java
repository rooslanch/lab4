package com.example.lab4.simulation.model.service;

import com.example.lab4.simulation.model.dto.FrictionDTO;
import com.example.lab4.simulation.model.dto.FrictionSectionDTO;

import java.util.Collections;

public class FrictionService {

    public static double getFrictionAt(FrictionDTO frictionDTO, double x) {
        var sections = frictionDTO.getSections();

        int index = Collections.binarySearch(sections, new FrictionSectionDTO(x, 0),
                (section1, section2) -> Double.compare(section1.getFromX(), section2.getFromX()));

        if (index < 0) {
            index = -index - 2;
        }

        if (index >= 0 && index < sections.size()) {
            return sections.get(index).getFriction();
        }
        return 0.0;
    }
}
