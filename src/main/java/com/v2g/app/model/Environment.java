package com.v2g.app.model;

import java.util.ArrayList;

public class Environment {
    private ArrayList<Building> buildings;
    private double current_temperature;

    public double current_temperature() {
        return current_temperature;
    }

    // TODO: maybe the computer can make the predicictions about which building is the best
    private class Building {
        private boolean is_default;
        // TODO: add a location object that will allow us to calculate the typical trip distance it takes
    }   
}