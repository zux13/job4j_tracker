package ru.job4j.checkstyle;

public class Broken {
    private int sizeOfEmpty = 10;
    String name;
    public static final String NEW_VALUE = "";
    public String surname;

    Broken() { }

    void echo() { }

    void media(Object obj) {
        if (obj != null) {
            System.out.println(obj);
        }
    }

    void method(int a, int b, int c, int d, int e, int f, int g) {

    }
}