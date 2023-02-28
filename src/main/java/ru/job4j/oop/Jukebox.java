package ru.job4j.oop;

public class Jukebox {

    public void music(int position) {
        switch (position) {
            case 1 -> System.out.println("Пусть бегут неуклюже");
            case 2 -> System.out.println("Спокойной ночи");
            default -> System.out.println("Песня не найдена");
        }
    }

    public static void main(String[] args) {
        Jukebox posOne = new Jukebox();
        Jukebox posTwo = new Jukebox();
        Jukebox posEleven = new Jukebox();
        posOne.music(1);
        posTwo.music(2);
        posEleven.music(11);
    }
}
