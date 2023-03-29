package ru.job4j.poly;

import java.math.BigDecimal;

public class Bus implements Transport {
    @Override
    public void ride() {
        System.out.println("Автобус делает вж-вж");
    }

    @Override
    public void passengers(int count) {
        System.out.println("Пассажиров в автобусе: " + count);
    }

    @Override
    public BigDecimal refuel(int fuel) {
        return BigDecimal.valueOf(fuel * 45.6);
    }

    public static void main(String[] args) {
        Transport bus = new Bus();
        bus.ride();
        bus.passengers(38);
        System.out.println("Стоимость 10 литров бензина на поездку: " + bus.refuel(10));
    }
}
