package ru.job4j.poly;

import java.math.BigDecimal;

public interface Transport {
    void ride();

    void passengers(int count);

    BigDecimal refuel(int fuel);
}
