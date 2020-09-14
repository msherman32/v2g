package com.v2g.app;

import javax.swing.plaf.basic.BasicScrollPaneUI.ViewportChangeHandler;
import javax.xml.transform.Templates;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.net.URL;
import java.net.URLConnection;

import com.v2g.app.model.Car;
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

        ArrayList<Car> cars = new ArrayList<Car>();
        cars.add(new Car());
        Employee adam = new Employee("Adam", 0, 300, 50, cars);
        Employee bob = new Employee("Bob", 0, 300, 50, cars);
        Employee charlie = new Employee("Charlie", 0, 300, 50, cars);
        v2g_System.registerComponent(adam);
        v2g_System.registerComponent(bob);
        v2g_System.registerComponent(charlie);
        try {
            adam.arriveAtChargingStation();
            bob.arriveAtChargingStation();
            charlie.arriveAtChargingStation();
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
            // if (num_secs > environment.getBuilding().get_seconds_open()) {
            if (num_secs > 3000) {
                typical_hours = false;
                adam.leaveChargingStation();
                bob.leaveChargingStation();
                charlie.leaveChargingStation();
            }

        } // End Cycle    
    }
}