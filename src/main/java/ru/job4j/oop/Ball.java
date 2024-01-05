package ru.job4j.oop;

public class Ball {

    public void tryRun(boolean condition) {
        String eatenOrRan = condition ? "Колобок съеден" : "Колобок сбежал";
        System.out.println(eatenOrRan);
    }
}
