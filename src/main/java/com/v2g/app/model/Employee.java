package com.v2g.app.model;

import java.time.zone.ZoneOffsetTransitionRule.TimeDefinition;
import java.util.ArrayList;
import java.util.Objects;

import com.v2g.app.App;
import com.v2g.app.model.v2g_system.mediator.Component;
import com.v2g.app.model.v2g_system.mediator.Mediator;

public class Employee implements Component {
    private Mediator system;
    private String employee_id;
    private String name;
    // private TimeDefinition expected_arrival;
    // private TimeDefinition expected_departure;
    private double expected_arrival;
    private double expected_departure;
    private double distance_to_home;
    private ArrayList<Car> cars;

    public Employee() {
    }

    public Employee(String name, double expected_arrival, double expected_departure,
            double distance_to_home, ArrayList<Car> cars) {
        this.name = name;
        this.employee_id = name;
        this.expected_arrival = expected_arrival;
        this.expected_departure = expected_departure;
        this.distance_to_home = distance_to_home;
        this.cars = cars;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    /**
     * This method returns the minimum charge needed for this particular car. First,
     * we calculate the distance needed to get from work to home and from home back
     * to work. Second, we calculate the ratio of distance travelable to a single
     * unit of charge. Third, we caluclate the minimum power required as a factor of
     * the distance and ratio + the power that will dissipate as a function of
     * temperature Finally, we multiply this minimum power by 3 to account for the
     * safety factor and return the percentage of the battery to charge
     */
    public double get_minimum_charge(double factor) throws Exception {
        if (factor < 1.0) {
            throw new Exception("Factor is less than 1.0.");
        }
        double mileage_required = this.distance_to_home * 2;
        double miles_per_unit_of_power_ratio = this.getCurrentCar().get_miles_driveable_at_capacity()
                / this.getCurrentCar().get_capacity();
        double power_required = (mileage_required / miles_per_unit_of_power_ratio)
                + this.getCurrentCar().getBattery().get_power_dissipation(App.temperature);
        double minimum_guaranteed_power = power_required * factor;
        return minimum_guaranteed_power >= this.getCurrentCar().get_capacity() 
            ? this.getCurrentCar().get_capacity() : minimum_guaranteed_power;
    }

    public boolean is_satisfied() throws Exception {
        return this.getCurrentCar().get_percentage() >= this.get_minimum_charge(3); // 1.5?
    }

    public double get_current_charge() {
        return this.getCurrentCar().get_current_charge();
    }

    public Car getCurrentCar() {
        return cars.get(0);
        // TODO: fix this logic!
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // public TimeDefinition getExpected_arrival() {
    //     return this.expected_arrival;
    // }

    // public void setExpected_arrival(TimeDefinition expected_arrival) {
    //     this.expected_arrival = expected_arrival;
    // }

    // public TimeDefinition getExpected_departure() {
    //     return this.expected_departure;
    // }

    // public void setExpected_departure(TimeDefinition expected_departure) {
    //     this.expected_departure = expected_departure;
    // }

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

    // public Employee expected_arrival(TimeDefinition expected_arrival) {
    //     this.expected_arrival = expected_arrival;
    //     return this;
    // }

    // public Employee expected_departure(TimeDefinition expected_departure) {
    //     this.expected_departure = expected_departure;
    //     return this;
    // }

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
        return Objects.equals(name, employee.name) && Objects.equals(expected_arrival, employee.expected_arrival)
                && Objects.equals(expected_departure, employee.expected_departure)
                && distance_to_home == employee.distance_to_home && Objects.equals(cars, employee.cars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, expected_arrival, expected_departure, distance_to_home, cars);
    }

    public void setMediator(Mediator mediator) {
        this.system = mediator;
    }

    public void leaveChargingStation() {
        system.leave(this);
    }

    public void arriveAtChargingStation() throws Exception {
        system.arrive(this);
    }

}