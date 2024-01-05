package ru.job4j.tracker;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemTest {
    
    @Test
    public void whenSortAscByNameTrue() {
        Item item1 = new Item("Молоток");
        Item item2 = new Item("Пила");
        Item item3 = new Item("Отвертка");
        List<Item> items = new ArrayList<>(List.of(item1, item2, item3));
        List<Item> expected = new ArrayList<>(List.of(item1, item3, item2));
        items.sort(new ItemAscByName());
        assertThat(items).isEqualTo(expected);
    }

    @Test
    public void whenSortDescByNameTrue() {
        Item item1 = new Item("Молоток");
        Item item2 = new Item("Пила");
        Item item3 = new Item("Отвертка");
        List<Item> items = new ArrayList<>(List.of(item1, item2, item3));
        List<Item> expected = new ArrayList<>(List.of(item2, item3, item1));
        items.sort(new ItemDescByName());
        assertThat(items).isEqualTo(expected);
    }

}