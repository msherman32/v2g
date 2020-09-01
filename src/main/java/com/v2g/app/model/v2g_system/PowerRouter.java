package com.v2g.app.model.v2g_system;

import java.util.ArrayList;

public class PowerRouter {

    private ArrayList<PowerCell> cells;

    public ArrayList<PowerCell> getCells() {
        return this.cells;
    }

    public double getTotalRequest() {
        double total = 0.0;
        for (PowerCell cell : cells) {
            total += cell.getRequest().request_level;
        }
        return total;
    }

    public class PowerCell {
        private double capacity;
        private Charge_Stats stats;
        private Request request;

        public void set_stats(double current_level, double minimum_level) {
            this.stats = new Charge_Stats();
            this.stats.setCurrent_level(current_level);
            this.stats.setMinimum_level(minimum_level);
        }
        
        public void prepare_request() { // TODO: put a goal object as a parameter?
            double difference = this.stats.getMinimum_level() - this.stats.getCurrent_level();
            Request request = new Request();
            request.setRequest_level(difference);            
        }

        public double getCapacity() {
            return capacity;
        }

        public Charge_Stats getStats() {
            return stats;
        }

        public Request getRequest() {
            return request;
        }
    }

    public class Charge_Stats {
        private double current_level;
        private double minimum_level;

        public double getCurrent_level() {
            return current_level;
        }
        public double getMinimum_level() {
            return minimum_level;
        }
        public void setMinimum_level(double minimum_level) {
            this.minimum_level = minimum_level;
        }
        public void setCurrent_level(double current_level) {
            this.current_level = current_level;
        }
       
    }
    
    public class Request {
        private double request_level;

        public double getRequest_level() {
            return request_level;
        }
        public void setRequest_level(double request_level) {
            this.request_level = request_level;
        }
    }
    
}