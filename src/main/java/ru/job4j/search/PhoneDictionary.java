package ru.job4j.search;

import java.util.ArrayList;
import java.util.function.*;

public class PhoneDictionary {
    private ArrayList<Person> persons = new ArrayList<>();

    public void add(Person person) {
        this.persons.add(person);
    }

    /**
     * Вернуть список всех пользователей, который содержат key в любых полях.
     * @param key Ключ поиска.
     * @return Список пользователей, которые прошли проверку.
     */
    public ArrayList<Person> find(String key) {
        Predicate<Person> nameContainsKey = x -> x.getName().contains(key);
        Predicate<Person> surnameContainsKey = x -> x.getSurname().contains(key);
        Predicate<Person> phoneContainsKey = x -> x.getPhone().contains(key);
        Predicate<Person> addressContainsKey = x -> x.getAddress().contains(key);
        ArrayList<Person> result = new ArrayList<>();
        for (Person value : persons) {
            if (nameContainsKey
                    .or(surnameContainsKey)
                    .or(phoneContainsKey)
                    .or(addressContainsKey)
                    .test(value)) {
                result.add(value);
            }
        }
        return result;
    }
}
