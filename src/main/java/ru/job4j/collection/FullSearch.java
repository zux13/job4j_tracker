package ru.job4j.collection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FullSearch {
    public Set<String> extractNumber(List<Task> tasks) {
        Set<String> rsl = new HashSet<>();
        for (Task task : tasks) {
            rsl.add(task.getNumber());
        }
        return rsl;
    }
}
