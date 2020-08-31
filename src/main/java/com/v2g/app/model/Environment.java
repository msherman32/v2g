package com.v2g.app.model;

import java.util.ArrayList;

public class Environment {
    private ArrayList<Building> buildings;
    private double current_temperature;

    public double current_temperature() {
        return current_temperature;
    }

    public Building getBuilding() {
        return buildings.get(0);
        // TODO: fix this
    }

    // TODO: maybe the computer can make the predicictions about which building is the best
    public class Building {
        private boolean is_default;
        private int seconds_open;
        // TODO: add a location object that will allow us to calculate the typical trip distance it takes

        public int get_seconds_open() {
            return seconds_open;
        }
    }   
}