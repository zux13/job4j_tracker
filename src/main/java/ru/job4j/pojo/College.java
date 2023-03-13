package ru.job4j.pojo;

import java.util.Date;

public class College {
    public static void main(String[] args) {
        Student student = new Student();
        student.setFio("Galiullin Rustem Rafaelevich");
        student.setGroup("Industrial and civil construction");
        student.setReceiptDate(new Date());

        System.out.println(student.getFio() + " studies " + student.getGroup() + " since " + student.getReceiptDate());
    }
}
