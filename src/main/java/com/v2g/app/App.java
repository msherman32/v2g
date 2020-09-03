package com.v2g.app;

import javax.xml.transform.Templates;

import java.io.InputStream;
import java.util.Scanner;
import java.net.URL;
import java.net.URLConnection;

import com.v2g.app.model.Employee;
import com.v2g.app.model.Environment;
import com.v2g.app.model.v2g_system.V2G_System;

import org.json.JSONObject;

import io.github.cdimascio.dotenv.Dotenv;

public class App {

    public static final int THIRTY_MINUTES = 1800; // in seconds
    public static double temperature;

    public static void main( String[] args )
    {
        // TODO: Setup environment from yaml
        Dotenv env = Dotenv.load();
        Environment environment = new Environment();
        temperature = environment.current_temperature();

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

        Employee adam = new Employee();
        try {
            adam.arriveAtChargingStation();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        while (v2g_System.cars_currently_charging() && typical_hours) { // TODO: fix this logic...

            // 1. Each charging station will send their stats to the router
            v2g_System.sendChargingStationStatsToRouter();
            
            // 2. The router will send its request to the power supply
            double power_requested = v2g_System.sendRouterRequestToPowerSupply();
            
            // 3. The power supply will send power to the router
            double power_returned = v2g_System.sendPowerSupplyResponseToRouter();
            
            // 4. The router will distribute the power to the charging stations
            v2g_System.sendRouterPowerToChargingStations(power_returned, power_requested);
            
            v2g_System.clear();

            num_secs++;
            if (num_secs > environment.getBuilding().get_seconds_open()) {
                typical_hours = false;
                adam.leaveChargingStation();
            }

        } // End Cycle    
    }
}