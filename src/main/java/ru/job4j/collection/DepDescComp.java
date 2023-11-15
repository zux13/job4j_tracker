package ru.job4j.collection;

import java.util.Comparator;

public class DepDescComp implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        String[] sub1 = o1.split("/", 2);
        String[] sub2 = o2.split("/", 2);
        if (sub2[0].compareTo(sub1[0]) == 0) {
            return (sub1.length > 1 ? sub1[1] : "").compareTo(sub2.length > 1 ? sub2[1] : "");
        } else {
            return sub2[0].compareTo(sub1[0]);
        }
    }
}