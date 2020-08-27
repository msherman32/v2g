package com.v2g.app;

import javax.xml.transform.Templates;

import com.v2g.app.model.Environment;
import com.v2g.app.model.v2g_system.ChargingStation;
import com.v2g.app.model.v2g_system.V2G_System;

public class App 
{
    public static void main( String[] args )
    {
        // TODO: Setup environment from yaml

        Environment environment = new Environment();
        double temperature = environment.current_temperature();

        V2G_System v2g_System = new V2G_System();
        boolean typical_hours = true;
        // final double minimum_percentage = 95;

        while (v2g_System.cars_at_charging_stations() && typical_hours) { // TODO: fix this

            // 1. Each charging station will send their stats to the router
            // double total_power_requested = 0.0;
            for (ChargingStation station : v2g_System.getCharging_stations()) {
                // send status to router
                if (station.is_occupied()) {
                    double minimum_percentage = station.getoccupant().get_minimum_charge(temperature);
                    if (!station.is_satisfied(minimum_percentage)) {
                        station.getoccupant().get_car().get_current_charge();
                        station.getoccupant().get_car().get_capacity();
                        station.getoccupant().get_car().get_miles_driveable_at_capacity();
                        // total_power_requested += station.getoccupant().
                    }
                }
            }
                        
            // 2. The router will send its request to the power supply
            // 3. The power supply will send power to the router
            // 4. The router will distribute the power to the charging stations

            for (ChargingStation station : v2g_System.getCharging_stations()) {
                station.getoccupant().get_car().getBattery().setValue;
                if (station.is_occupied()) {
                    double minimum_percentage = station.getoccupant().get_minimum_charge(temperature);
                    if (station.is_satisfied(minimum_percentage)) {
                        // remove from some queue
                    } else {
                        // do something else
                    }
                }
            }
            

            if (num_iterations > some_amount) {
                typical_hours = false;
            }



        }

        

    }
}
