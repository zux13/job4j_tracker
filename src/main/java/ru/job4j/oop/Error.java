package ru.job4j.oop;

public class Error {
    private boolean active;
    private int status;
    private String message;

    public Error(boolean active, int status, String message) {
        this.active = active;
        this.status = status;
        this.message = message;
    }

    public Error() {
    }

    public void printInfo() {
        System.out.println("Активность: " + active);
        System.out.println("Статус: " + status);
        System.out.println("Описание: " + message);
    }

    public static void main(String[] args) {
        Error errOne = new Error();
        Error errTwo = new Error(true, 1, "Initializing error");
        Error errThree = new Error(true, 2, "Not found");
        Error errFour = new Error(true, 3, "Invalid code");
        errOne.printInfo();
        errTwo.printInfo();
        errThree.printInfo();
        errFour.printInfo();
    }
}
