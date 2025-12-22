package com.example.lab4.simulation.io.dto;

import java.util.List;

public class FrictionJsonDTO {

    public static class Section {
        public double fromX;
        public double mu;
    }

    public List<Section> sections;
}
