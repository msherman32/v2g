package com.v2g.app.model;

import java.util.Objects;

public class Car {
    private Battery battery;

    public Car() {
    }

    public Car(Battery battery) {
        this.battery = battery;
    }

    public Battery getBattery() {
        return this.battery;
    }

    public void setBattery(Battery battery) {
        this.battery = battery;
    }

    public double get_percentage() {
        return battery.current_charge / battery.capacity;
    }

    public double get_capacity() {
        return battery.capacity;
    }

    public double get_current_charge() {
        return battery.current_charge;   
    }

    public double get_miles_driveable_at_capacity() {
        return battery.miles_driveable_at_capacity;
    }  

    public Car battery(Battery battery) {
        this.battery = battery;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Car)) {
            return false;
        }
        Car car = (Car) o;
        return Objects.equals(battery, car.battery);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(battery);
    }

    @Override
    public String toString() {
        return "{" +
            " battery='" + getBattery() + "'" +
            "}";
    }


    public class Battery {
        private double capacity; // kWh?
        private double current_charge;
        private double miles_driveable_at_capacity;
        private double power_dissipation;

        public double get_power_dissipation(double temperature) {
            return power_dissipation;
            // TODO: fix this
        }

        public double get_capacity() {
            return capacity;
        }

        public double get_current_charge() {
            return current_charge;   
        }

        public double get_miles_driveable_at_capacity() {
            return miles_driveable_at_capacity;
        }        
    }

}