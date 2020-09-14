package com.v2g.app.model.v2g_system;

public class PowerSupply {
    private double max_power_supply_rate; // KW per second?

    public double getMax_power_supply_rate() {
        return max_power_supply_rate;
    }

	public double send_power_supply_based_on(double request) {
        double result = request >= this.max_power_supply_rate ? this.max_power_supply_rate : request;
        System.out.println(result);
        return result;
    }
    
}