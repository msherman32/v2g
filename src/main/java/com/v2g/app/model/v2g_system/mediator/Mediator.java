package com.v2g.app.model.v2g_system.mediator;

public interface Mediator {
    void registerComponent(Component component);

    void arrive(String employee_id);
    void leave(String employee_id);

    void sendChargingStationStatsToRouter(double temperature);

    double sendRouterRequestToPowerSupply();
    
    double sendPowerSupplyResponseToRouter();

    void sendRouterPowerToChargingStations(double power_retunred, double power_requested);
    
    void clear();
}