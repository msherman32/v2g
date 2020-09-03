package com.v2g.app.model.v2g_system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.SortedSet;

import com.v2g.app.App;
import com.v2g.app.model.Employee;
import com.v2g.app.model.v2g_system.PowerRouter.PowerCell;
import com.v2g.app.model.v2g_system.mediator.Component;
import com.v2g.app.model.v2g_system.mediator.Mediator;

public class V2G_System implements Mediator {

    // Define a better power NETWORK here
    private ArrayList<PowerSupply> sources;
    private ArrayList<PowerRouter> routers;
    private int num_charging_stations; // Max value
    private HashMap<String, Integer> charging_stations;
    private HashMap<String, Employee> employees;
    private SortedSet<Integer> availableStations;

    public V2G_System() {
    }

    public V2G_System(ArrayList<PowerSupply> sources, ArrayList<PowerRouter> routers, HashMap<ChargingStation, Employee> charging_stations) {
        this.sources = sources;
        this.routers = routers;
        // this.charging_stations = charging_stations;
    }

    public ArrayList<PowerSupply> getSources() {
        return this.sources;
    }

    public PowerSupply getPowerSource() {
        return this.sources.get(0);
    }

    // TODO: be able to send this request to a particular power supply or a matrix of supplies
    // public double send_request(double request) {
    //     return request >= this.getPowerSource().getMax_power_supply_rate()
    //             ? this.getPowerSource().getMax_power_supply_rate()
    //             : request;
    // }

    public void setSources(ArrayList<PowerSupply> sources) {
        this.sources = sources;
    }

    public ArrayList<PowerRouter> getRouters() {
        return this.routers;
    }

    public PowerRouter getRouter() {
        return this.routers.get(0);
    }

    public PowerCell getCell(int index) {
        return this.getRouter().getCells().get(index);
    }

    public void setRouters(ArrayList<PowerRouter> routers) {
        this.routers = routers;
    }

    public V2G_System sources(ArrayList<PowerSupply> sources) {
        this.sources = sources;
        return this;
    }

    public V2G_System routers(ArrayList<PowerRouter> routers) {
        this.routers = routers;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof V2G_System)) {
            return false;
        }
        V2G_System v2G_System = (V2G_System) o;
        return Objects.equals(sources, v2G_System.sources) && Objects.equals(routers, v2G_System.routers)
                && Objects.equals(charging_stations, v2G_System.charging_stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sources, routers, charging_stations);
    }

    @Override
    public String toString() {
        return "{" + " sources='" + getSources() + "'" + ", routers='" + getRouters() + "'" + ", charging_stations='" + "}";
    }

    public boolean cars_currently_charging() {
        return !this.charging_stations.isEmpty();
    }

    public void registerComponent(Component component) {
        component.setMediator(this);
        // TODO instantiate components based on their types
    }
    
    public void sendChargingStationStatsToRouter() {
        for (String gid : this.charging_stations.keySet()) { // Contains all the gids of employees currently occupying a charging station 
            Integer station = this.charging_stations.get(gid);
            Employee occupant = this.employees.get(gid); 
            try {
                if (!occupant.is_satisfied()) {
                    this.sendStationDataToRouter(station, occupant);
                } else {
                    // Do some v2g stuff...
                    // Or stop chargin this one.
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // Send current charge to router's cell that matches this charging station (1:1 ratio)
    private void sendStationDataToRouter(int cell, Employee occupant) throws Exception {
        this.getRouter().getCells().get(cell).set_stats(occupant.get_current_charge(), occupant.get_minimum_charge(3));
    }

    public double sendRouterRequestToPowerSupply() {
        for (PowerCell power_cell : this.getRouter().getCells()) {
            power_cell.prepare_request();
        }
        return this.getRouter().getTotalRequest(); // TODO: if this is 0, don't send a request to the power supply at all? (or do we need this for statistic purposes?)
    }
    
    public double sendPowerSupplyResponseToRouter() {
        return this.getPowerSource().send_power_supply_based_on(this.getRouter().getTotalRequest());
    }
    
    public void sendRouterSupplyToStation(double power) {
        
    }
    
    public void clear() {
        this.getRouter().clearCells();
    }

    /**
     * Send the desired power to the charging stations that requested them
    **/
     public void sendRouterPowerToChargingStations(double power_retunred, double power_requested) {
        // TODO: change this so that we send the charge from the power cell TO the charging station instead of vice versa...
        if (power_retunred == power_requested) {
            for (String gid : this.charging_stations.keySet()) { // Contains all the gids of employees currently occupying a charging station 
                Integer station = this.charging_stations.get(gid);
                Employee occupant = this.employees.get(gid); 
                try {
                    if (occupant.is_satisfied()) {
                        this.sendStationDataToRouter(station, occupant);
                    } else {
                        // Do some v2g stuff...
                        // Or stop charging this one.
                        // remove from the charging station list..?
                        // Or move into low power mode until 100%
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            // Clear each current stats and request in each power cell

        } else { // Will be less than that supplied
            // 1. Put all the ones that are less than thirty minutes away in a hashmap or mark them somehow
            // 2. Put the rest aside. Based on the needs of ALL the cars, divide power for next thirty minutes so that around 75% goes to the ones leaving soon
            // and so that the remaining 25% is sent to the other cars NOT leaving soon. Then, once they reach a certain threshold resume power.
            // Decide based on time which ones will be leaving soon and divide it up this way
            
            for (String gid : this.charging_stations.keySet()) { // Contains all the gids of employees currently occupying a charging station 
                Integer station = this.charging_stations.get(gid);
                Employee occupant = this.employees.get(gid); 
                try {
                    if (occupant.is_satisfied()) {
                        this.sendStationDataToRouter(station, occupant);
                    } else {
                        // Do some v2g stuff...
                        // Or stop charging this one.
                        // remove from the charging station list..?
                        // Or move into low power mode until 100%
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
                // this.getRouter().getCells().get(0).getRequest();
                // int seconds_until_departure = station.getoccupant().getExpected_departure() - num_secs;
                // if (seconds_until_departure <= App.THIRTY_MINUTES) {
                //     // Make priority
                //     // 1. Check to see if we can get it to at least a factor of 2 instead of 3.
                //     // 2. Check to see if we can get it to at least a factor of 1.5 instead?
                //     // 3. Divert large percentages of power to this particular car

                //     double factor_2 = station.getoccupant().get_minimum_charge(2, temperature);
                //     double factor_1_5 = station.getoccupant().get_minimum_charge(1.5, temperature);
                //     if ( Math.abs(factor_2 - station.get_default_car().get_current_charge()) 
                //         > Math.abs(factor_1_5 - station.get_default_car().get_current_charge()) ) { // Closer to 2_factor
                //         // stop charging...
                //     } else { // Closer to 1.5_factor
                //         // keep charging
                //     }

                // }
                
            // }   
        }
    }

    public void leave(String employee_id) {
        Integer station = this.charging_stations.remove(employee_id);
        if (station != null) {
            this.availableStations.add(station);
        }
        // Or have this only be called at the tail end of the while loop and THEN be reflected in the next iteration of the loop
    }

    public void arrive(String employee_id) throws Exception {
        if (this.availableStations.isEmpty()) {
            throw new Exception("All Charging Stations are filled. Come back later!");
        }
        int station = this.availableStations.first();
        this.availableStations.remove(station);
        this.charging_stations.put(employee_id, station);
        // Calculate something and send it to the router...?
    }
    
}