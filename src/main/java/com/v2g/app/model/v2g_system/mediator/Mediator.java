package com.v2g.app.model.v2g_system.mediator;

public interface Mediator {
    void registerComponent(Component component);

    void arrive(String employee_id) throws Exception;
    void leave(String employee_id);

    void sendChargingStationStatsToRouter();

    double sendRouterRequestToPowerSupply();
    
    double sendPowerSupplyResponseToRouter();

    void sendRouterPowerToChargingStations(double power_returned, double power_requested);
    
    void clear();
}