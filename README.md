# v2g

To run the app via maven command:
```
mvn exec:java -Dexec.mainClass=com.v2g.app.App
```

Cool existing simulation: https://acnportal.readthedocs.io/en/latest/index.html
https://ev.caltech.edu/assets/pub/ACN_Sim_Open_Source_Simulator.pdf

Demand Response Control for PHEV Charging Stations by Dynamic Price Adjustments

Method to evenly distribute charge to each car:
Each second (represented by one iteration of the main loop), the charging stations that hold cars that are not satisfied should send information to their corresponding power cell:
    1. Their current charge level
    2. Their demanded charge level
    3. The amount of time left at the charging station

If the v2g system can continue to supply power per second less than the max charging rate to these vehicles then it will continue to do so. Otherwise, we must decide based on priority what percentage of charge should go to what cars (based on the most charge needed versus time left at the charging station).