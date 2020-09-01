package com.v2g.app.model;

import java.time.zone.ZoneOffsetTransitionRule.TimeDefinition;
import java.util.ArrayList;
import java.util.Objects;

public class Employee {
    private String name; // Used to ID people in the system
    private TimeDefinition expected_arrival;
    private TimeDefinition expected_departure;
    private double distance_to_home;
    private ArrayList<Car> cars;

    public Employee() {
    }

    public Employee(String name, TimeDefinition expected_arrival, TimeDefinition expected_departure, double distance_to_home, ArrayList<Car> cars) {
        this.name = name;
        this.expected_arrival = expected_arrival;
        this.expected_departure = expected_departure;
        this.distance_to_home = distance_to_home;
        this.cars = cars;
    }

    /**
     * This method returns the minimum charge needed for this particular car.
     * First, we calculate the distance needed to get from work to home and from home back to work.
     * Second, we calculate the ratio of distance travelable to a single unit of charge.
     * Third, we caluclate the minimum power required as a factor of the distance and ratio + the power that will dissipate as a function of temperature
     * Finally, we multiply this minimum power by 3 to account for the safety factor and return the percentage of the battery to charge
     */
    public double get_minimum_charge(double temperature) {
        double mileage_required = this.distance_to_home * 2;
        double miles_per_unit_of_power_ratio = this.get_car().get_miles_driveable_at_capacity() / this.get_car().get_capacity();
        double power_required = (mileage_required / miles_per_unit_of_power_ratio) + this.get_car().getBattery().get_power_dissipation(temperature);
        double minimum_guaranteed_power = 3 * power_required;
        return minimum_guaranteed_power >= this.get_car().get_capacity() ? 1.0 : minimum_guaranteed_power / this.get_car().get_capacity(); 
    }

    public Car get_car() {
        return cars.get(0);
        // TODO: fix this logic!
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TimeDefinition getExpected_arrival() {
        return this.expected_arrival;
    }

    public void setExpected_arrival(TimeDefinition expected_arrival) {
        this.expected_arrival = expected_arrival;
    }

    public TimeDefinition getExpected_departure() {
        return this.expected_departure;
    }

    public void setExpected_departure(TimeDefinition expected_departure) {
        this.expected_departure = expected_departure;
    }

    public double getDistance_to_home() {
        return this.distance_to_home;
    }

    public void setDistance_to_home(double distance_to_home) {
        this.distance_to_home = distance_to_home;
    }

    public ArrayList<Car> getCars() {
        return this.cars;
    }

    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }

    public Employee name(String name) {
        this.name = name;
        return this;
    }

    public Employee expected_arrival(TimeDefinition expected_arrival) {
        this.expected_arrival = expected_arrival;
        return this;
    }

    public Employee expected_departure(TimeDefinition expected_departure) {
        this.expected_departure = expected_departure;
        return this;
    }

    public Employee distance_to_home(double distance_to_home) {
        this.distance_to_home = distance_to_home;
        return this;
    }

    public Employee cars(ArrayList<Car> cars) {
        this.cars = cars;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Employee)) {
            return false;
        }
        Employee employee = (Employee) o;
        return Objects.equals(name, employee.name) && Objects.equals(expected_arrival, employee.expected_arrival) && Objects.equals(expected_departure, employee.expected_departure) && distance_to_home == employee.distance_to_home && Objects.equals(cars, employee.cars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, expected_arrival, expected_departure, distance_to_home, cars);
    }

    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", expected_arrival='" + getExpected_arrival() + "'" +
            ", expected_departure='" + getExpected_departure() + "'" +
            ", distance_to_home='" + getDistance_to_home() + "'" +
            ", cars='" + getCars() + "'" +
            "}";
    }

}