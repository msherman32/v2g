package com.v2g.app.model.v2g_system;

import java.util.Objects;

import com.v2g.app.model.Car;
import com.v2g.app.model.Employee;

public class ChargingStation {

    private Employee occupant;
   
    public boolean is_occupied() {
        return occupant != null;
    }

    public boolean is_satisfied(double minimum_value) {
        return occupant.get_car().get_percentage() >= minimum_value; 
    }

    public Car get_default_car() {
        return occupant.get_car();
    }

    public ChargingStation() {
    }

    public ChargingStation(Employee occupant) {
        this.occupant = occupant;
    }

    public Employee getoccupant() {
        return this.occupant;
    }

    public void setoccupant(Employee occupant) {
        this.occupant = occupant;
    }

    public ChargingStation occupant(Employee occupant) {
        this.occupant = occupant;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ChargingStation)) {
            return false;
        }
        ChargingStation chargingStation = (ChargingStation) o;
        return Objects.equals(occupant, chargingStation.occupant);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(occupant);
    }

    @Override
    public String toString() {
        return "{" +
            " occupant='" + getoccupant() + "'" +
            "}";
    }

    
}