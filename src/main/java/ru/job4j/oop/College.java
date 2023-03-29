package ru.job4j.oop;

public class College {
    public static void main(String[] args) {
        /* Все приведения являются повышающими (up casting) */
        Freshman petya = new Freshman();
        Student student = petya;
        Object obj = student;
    }
}
