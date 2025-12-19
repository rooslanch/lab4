package com.example.lab4.simulation.model.dto;

import com.example.lab4.simulation.model.dto.FrictionDTO;
import com.example.lab4.simulation.model.dto.FrictionSectionDTO;

import java.util.Collections;

public class FrictionService {

    public static double getFrictionAt(FrictionDTO frictionDTO, double x) {
        var sections = frictionDTO.getSections();

        // Используем бинарный поиск для поиска секции, где fromX <= x
        int index = Collections.binarySearch(sections, new FrictionSectionDTO(x, 0),
                (section1, section2) -> Double.compare(section1.getFromX(), section2.getFromX()));

        // Если индекс отрицательный, значит секция не найдена, определяем позицию для вставки
        if (index < 0) {
            index = -index - 2; // Получаем индекс последней секции, которая имеет fromX <= x
        }

        // Если индекс валиден, возвращаем значение трения для найденной секции
        if (index >= 0 && index < sections.size()) {
            return sections.get(index).getFriction();  // Возвращаем трение для найденной секции
        }

        // Если секция не найдена, возвращаем значение по умолчанию
        return 0.0; // Без трения по умолчанию
    }
}
