package ru.job4j.lombok;

public class LombokUsage {
    public static void main(String[] args) {
        var permission = Permission.of()
                .id(1)
                .name("test")
                .rules("first")
                .rules("second")
                .rules("third")
                .build();
        System.out.println(permission);
    }
}
