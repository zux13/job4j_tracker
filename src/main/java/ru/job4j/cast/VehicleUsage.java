package ru.job4j.cast;

public class VehicleUsage {
    public static void main(String[] args) {
        Vehicle bus = new Bus();
        Vehicle plane = new Plane();
        Vehicle train = new Train();
        Vehicle[] vhc = {bus, plane, train};
        for (Vehicle vehicle : vhc) {
            vehicle.move();
        }
    }
}
