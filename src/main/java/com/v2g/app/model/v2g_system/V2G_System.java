package com.v2g.app.model.v2g_system;

import java.util.ArrayList;
import java.util.Objects;

public class V2G_System {

    // Define power network here
    private ArrayList<PowerSupply> sources;
    private ArrayList<PowerRouter> routers;
    private ArrayList<ChargingStation> charging_stations;

    public V2G_System() {
    }

    public V2G_System(ArrayList<PowerSupply> sources, ArrayList<PowerRouter> routers, ArrayList<ChargingStation> charging_stations) {
        this.sources = sources;
        this.routers = routers;
        this.charging_stations = charging_stations;
    }

    public ArrayList<PowerSupply> getSources() {
        return this.sources;
    }

    public void setSources(ArrayList<PowerSupply> sources) {
        this.sources = sources;
    }

    public ArrayList<PowerRouter> getRouters() {
        return this.routers;
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
        return Objects.equals(sources, v2G_System.sources) && Objects.equals(routers, v2G_System.routers) && Objects.equals(charging_stations, v2G_System.charging_stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sources, routers, charging_stations);
    }

    @Override
    public String toString() {
        return "{" +
            " sources='" + getSources() + "'" +
            ", routers='" + getRouters() + "'" +
            ", charging_stations='" + getCharging_stations() + "'" +
            "}";
    }    
    
    public boolean cars_at_charging_stations() {
        for (ChargingStation station : charging_stations) {
            if (station.is_occupied()) {
                return true;
            }
        }
        return false;
    }
}