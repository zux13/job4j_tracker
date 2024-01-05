package ru.job4j.collection;

import java.util.Comparator;

public class LexSort implements Comparator<String> {

    @Override
    public int compare(String left, String right) {
        String[] leftChars = left.split(". ");
        String[] rightChars = right.split(". ");
        return Integer.compare(Integer.parseInt(leftChars[0]), Integer.parseInt(rightChars[0]));
    }
}