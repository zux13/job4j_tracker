package ru.job4j.ex;

public class FactRec {
    public static int calc(int n) {
        return n == 0 || n == 1 ? 1 : calc(n - 1) * n;
    }

    public static void main(String[] args) {
        int rsl = calc(3);
        System.out.println(rsl);
    }
}