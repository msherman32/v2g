package com.v2g.app.model.v2g_system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import com.v2g.app.model.v2g_system.PowerRouter.PowerCell;
import com.v2g.app.model.v2g_system.mediator.Component;
import com.v2g.app.model.v2g_system.mediator.Mediator;

public class V2G_System implements Mediator {

    // Define power network here
    private ArrayList<PowerSupply> sources;
    private ArrayList<PowerRouter> routers;
    private HashMap<String, ChargingStation> charging_stations;

    public V2G_System() {
    }

    public V2G_System(ArrayList<PowerSupply> sources, ArrayList<PowerRouter> routers, HashMap<String, ChargingStation> charging_stations) {
        this.sources = sources;
        this.routers = routers;
        this.charging_stations = charging_stations;
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

    public ArrayList<ChargingStation> getCharging_stations() {
        return this.charging_stations;
    }

    public void setCharging_stations(ArrayList<ChargingStation> charging_stations) {
        this.charging_stations = charging_stations;
    }

    public V2G_System sources(ArrayList<PowerSupply> sources) {
        this.sources = sources;
        return this;
    }

    public V2G_System routers(ArrayList<PowerRouter> routers) {
        this.routers = routers;
        return this;
    }

    public V2G_System charging_stations(ArrayList<ChargingStation> charging_stations) {
        this.charging_stations = charging_stations;
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
        return "{" + " sources='" + getSources() + "'" + ", routers='" + getRouters() + "'" + ", charging_stations='"
                + getCharging_stations() + "'" + "}";
    }

    public boolean cars_currently_charging() {
        for (ChargingStation station : charging_stations) {
            if (station.is_occupied()) {
                return true;
            }
        }
        return false;
    }

    public void registerComponent(Component component) {
        component.setMediator(this);
        // TODO instantiate components based on their types
    }

    
    public void sendChargingStationStatsToRouter(double temperature) {
        int cell = 0;
        for (ChargingStation station : this.charging_stations) {
            if (station.is_occupied()) {
                try {
                    double minimum_percentage = station.getoccupant().get_minimum_charge(3, temperature);
                    if (!station.is_satisfied(minimum_percentage)) {
                        this.sendStationDataToRouter(cell, station.get_current_charge(), minimum_percentage);
                    } else {
                        // Do some v2g stuff...
                        // Or stop chargin this one.
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            cell++;
        }
    }

    // Send current charge to router's cell that matches this charging station (1:1 ratio)
    private void sendStationDataToRouter(int cell, double current_charge, double minimum_charge) {
        this.getRouter().getCells().get(cell).set_stats(current_charge, minimum_charge);
    }

    public double sendRouterRequestToPowerSupply() {
        for (PowerCell power_cell : this.getRouter().getCells()) {
            power_cell.prepare_request();
        }
        return this.getRouter().getTotalRequest();
    }
    
    public double sendPowerSupplyResponseToRouter() {
        return this.getPowerSource().send_power_supply_based_on(this.getRouter().getTotalRequest());
    }
    
    public void sendRouterSupplyToStation(double power) {
        
    }
    
    public void clear() {
        this.getRouter().clearCells();
        this.charging_stations.clear();
    }

    public void sendRouterPowerToChargingStations(double power_retunred, double power_requested) {
        int cell = 0;
        if (power_retunred == power_requested) {
            // Send the desired power to the charging stations that requested them
            for (ChargingStation station : this.charging_stations) {
                // station.getoccupant().get_car().getBattery().setValue;
                if (station.is_occupied()) {
                    double minimum_percentage = station.getoccupant().get_minimum_charge(3, temperature);
                    if (station.is_satisfied(minimum_percentage)) {
                        // remove from some queue
                    } else {
                        // do something else
                        this.sendRouterSupplyToStation(cell, power_returned / x);
                    }
                }
                cell++;
            }

            // Clear each current stats and request in each power cell

        } else { // Will be less than that supplied
            // 1. Put all the ones that are less than thirty minutes away in a hashmap or mark them somehow
            // 2. Put the rest aside. Based on the needs of ALL the cars, divide power for next thirty minutes so that around 75% goes to the ones leaving soon
            // and so that the remaining 25% is sent to the other cars NOT leaving soon. Then, once they reach a certain threshold resume power.
            // Decide based on time which ones will be leaving soon and divide it up this way
            for (ChargingStation station : v2g_System.getCharging_stations()) {
                v2g_System.getRouter().getCells().get(0).getRequest();
                int seconds_until_departure = station.getoccupant().getExpected_departure() - num_secs;
                if (seconds_until_departure <= THIRTY_MINUTES) {
                    // Make priority
                    // 1. Check to see if we can get it to at least a factor of 2 instead of 3.
                    // 2. Check to see if we can get it to at least a factor of 1.5 instead?
                    // 3. Divert large percentages of power to this particular car

                    double factor_2 = station.getoccupant().get_minimum_charge(2, temperature);
                    double factor_1_5 = station.getoccupant().get_minimum_charge(1.5, temperature);
                    if ( Math.abs(factor_2 - station.get_default_car().get_current_charge()) 
                        > Math.abs(factor_1_5 - station.get_default_car().get_current_charge()) ) { // Closer to 2_factor
                        // stop charging...
                    } else { // Closer to 1.5_factor
                        // keep charging
                    }

                }
                
            }   
        }
    }

    // TODO: change it so that ctrl+space switches with control + period in vscode...
    // How to handle if a charging station is occupied?
    public void leave(String employee_id) {
        if (this.charging_stations.get(employee_id) != null) {
            this.charging_stations.remove(employee_id);
        }
    }

    public void arrive(String employee_id) {
        this.charging_stations.put(employee_id, new ChargingStation());
    }
    
}