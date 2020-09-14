package com.v2g.app.model.v2g_system.mediator;

import com.v2g.app.model.Employee;

public interface Mediator {
    void registerComponent(Component component);

    void arrive(Employee employee) throws Exception;
    void leave(Employee employee);

    void sendChargingStationStatsToRouter();

    double sendRouterRequestToPowerSupply();
    
    double sendPowerSupplyResponseToRouter();

    void sendRouterPowerToChargingStations(double power_returned, double power_requested);
    
    void clear();
}