package com.v2g.app.model.v2g_system;

import java.util.ArrayList;

public class PowerRouter {

    private ArrayList<PowerCell> cells;

    public PowerRouter() {
        this.cells = new ArrayList<PowerCell>(5);
    }

    public ArrayList<PowerCell> getCells() {
        return this.cells;
    }

    public void clearCells() {
        for (PowerCell powerCell : this.cells) {
            powerCell.clear();
        }
        // this.clear(); // TODO ??
    }

    // TODO: factor in time here?
    public double getTotalRequest() {
        double total = 0.0;
        for (PowerCell cell : cells) {
            total += cell.getRequest().getRequest_level();
        }
        return total;
    }

    public class PowerCell {
        private double capacity;
        private Charge_Stats stats;
        private Request request;

        public PowerCell() {
            this.capacity = 50;
        }

        public void clear() {
            this.stats = null;
        }

        public void set_stats(double current_level, double minimum_level) {
            this.stats = new Charge_Stats();
            this.stats.setCurrent_level(current_level);
            this.stats.setMinimum_level(minimum_level);
        }

        // TODO: put a goal object as a parameter?
        public void prepare_request() {
            double difference = this.stats.getMinimum_level() - this.stats.getCurrent_level();
            this.request = new Request();
            this.request.setRequest_level(difference);
            this.request.setNum_secs_left(this.stats.getNum_secs_left());
            // System.out.println("HERE" + this.request);
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
        private int num_secs_left;

        public int getNum_secs_left() {
            return num_secs_left;
        }

        public void setNum_secs_left(int num_secs_left) {
            this.num_secs_left = num_secs_left;
        }

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
        private double num_secs_left;

        public double getRequest_level() {
            return request_level;
        }

        public double getNum_secs_left() {
            return num_secs_left;
        }

        public void setRequest_level(double request_level) {
            this.request_level = request_level;
        }

        public void setNum_secs_left(double num_secs_left) {
            this.num_secs_left = num_secs_left;
        }


        @Override
        public String toString() {
            return "Request: " + 
                String.format("%d kWh in %d seconds", this.request_level, this.num_secs_left);
        }
        

    }

    public PowerCell getCell(int index) {
        return this.cells.get(index);
    }
    
}