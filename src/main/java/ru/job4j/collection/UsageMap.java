package ru.job4j.collection;

import java.util.HashMap;

public class UsageMap {
    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<>();
        map.put("zux-rr@ya.ru", "Ilnur Safin");
        map.put("zux-rr@ya.ru", "Rustem Galiullin");
        map.put("parsentev@yandex.ru", "Petr Arsentev");
        map.put("igorjr@yandex.ru", "Igor Krimsky");
        for (String key: map.keySet()) {
            System.out.println(key + " = " + map.get(key));
        }
    }
}
