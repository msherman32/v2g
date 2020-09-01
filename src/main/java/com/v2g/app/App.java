package com.v2g.app;

import javax.xml.transform.Templates;

import java.io.InputStream;
import java.util.Scanner;
import java.net.URL;
import java.net.URLConnection;

import com.v2g.app.model.Environment;
import com.v2g.app.model.v2g_system.ChargingStation;
import com.v2g.app.model.v2g_system.V2G_System;
import com.v2g.app.model.v2g_system.PowerRouter.*;

import org.json.JSONObject;

import io.github.cdimascio.dotenv.Dotenv;

public class App {
    public static void main( String[] args )
    {
        // TODO: Setup environment from yaml
        Dotenv env = Dotenv.load();
        Environment environment = new Environment();
        double temperature = environment.current_temperature();

        // Get Actual Temperature:
        try {
            String url = "http://api.openweathermap.org/data/2.5/weather";
            String api_key = env.get("weather_api_key");
            // String charset = "UTF-8";
            // String query = String.format("q=%s&appid=%s", URLEncoder.encode("Dunwoody", charset), URLEncoder.encode(api_key, charset));
            String query = String.format("q=%s&appid=%s", "Dunwoody", api_key);
            URLConnection connection = new URL(url + "?" + query).openConnection();
            // connection.setRequestProperty("Accept-Charset", charset);
            InputStream response= connection.getInputStream();
            Scanner scanner = new Scanner(response);
            String responseBody = scanner.useDelimiter("\\A").next();
            JSONObject json = new JSONObject(responseBody);
            double celsius = ((Double) json.getJSONObject("main").get("temp")) - 273.16;
            System.out.println(celsius);
            temperature = celsius;
            scanner.close();
            response.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        V2G_System v2g_System = new V2G_System();
        boolean typical_hours = true;
        // final double minimum_percentage = 95;
        int num_secs = 0;

        while (v2g_System.cars_currently_charging() && typical_hours) { // TODO: fix this

            // 1. Each charging station will send their stats to the router
            // double total_power_requested = 0.0;
            int cell = 0;
            for (ChargingStation station : v2g_System.getCharging_stations()) {
                if (station.is_occupied()) {
                    double minimum_percentage = station.getoccupant().get_minimum_charge(temperature);
                    if (!station.is_satisfied(minimum_percentage)) {
                        // Send current charge to router's cell that matches this charging station (1:1 ratio)
                        v2g_System.getRouter().getCells().get(cell).set_stats(station.getoccupant().get_car().get_current_charge(), minimum_percentage);
                    } else {
                        // Do some v2g stuff...
                    }
                }
                cell++;
            }
            
            // 2. The router will send its request to the power supply
            for (PowerCell power_cell : v2g_System.getRouter().getCells()) {
                power_cell.prepare_request();
            }
            double requested_Charge = v2g_System.getRouter().getTotalRequest();
            
            // 3. The power supply will send power to the router
            double power_returned = v2g_System.send_request(requested_Charge);
            
            // 4. The router will distribute the power to the charging stations
            if (power_returned == requested_Charge) {
                // Send the desired power to the charging stations that requested them
                for (ChargingStation station : v2g_System.getCharging_stations()) {
                    // station.getoccupant().get_car().getBattery().setValue;
                    if (station.is_occupied()) {
                        double minimum_percentage = station.getoccupant().get_minimum_charge(temperature);
                        if (station.is_satisfied(minimum_percentage)) {
                            // remove from some queue
                        } else {
                            // do something else
                        }
                    }
                }

                // Clear each current stats and request in each power cell

            } else { // Will be less than that supplied
                // Decide based on time which ones will be leaving soon and divide it up this way


            }
            

            num_secs++;
            if (num_secs > environment.getBuilding().get_seconds_open()) {
                typical_hours = false;
            }

        } // End Cycle    
    }
}