package ru.job4j.tracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HbmTrackerTest {

    @Test
    public void whenAddNewItemThenTrackerHasSameItem() throws Exception {
        try (var tracker = new HbmTracker()) {
            Item item = new Item();
            item.setName("test1");
            tracker.add(item);
            Item result = tracker.findById(item.getId());
            assertThat(result.getName()).isEqualTo(item.getName());
        }
    }

    @Test
    public void whenReplaceThenItemIsUpdated() throws Exception {
        try (var tracker = new HbmTracker()) {
            Item item = new Item();
            item.setName("old");
            item.setCreated(LocalDateTime.now());
            tracker.add(item);

            Item updated = new Item();
            updated.setName("new");
            updated.setCreated(LocalDateTime.now().plusDays(1));
            boolean result = tracker.replace(item.getId(), updated);

            Item fromDb = tracker.findById(item.getId());

            assertThat(result).isTrue();
            assertThat(fromDb.getName()).isEqualTo("new");
        }
    }

    @Test
    public void whenDeleteThenItemIsGone() throws Exception {
        try (var tracker = new HbmTracker()) {
            Item item = new Item();
            item.setName("toDelete");
            tracker.add(item);
            tracker.delete(item.getId());
            Item fromDb = tracker.findById(item.getId());
            assertThat(fromDb).isNull();
        }
    }

    @Test
    public void whenFindAllThenReturnListOfItems() throws Exception {
        try (var tracker = new HbmTracker()) {
            tracker.add(new Item("item1"));
            tracker.add(new Item("item2"));
            List<Item> result = tracker.findAll();
            assertThat(result.size()).isGreaterThanOrEqualTo(2);
        }
    }

    @Test
    public void whenFindByNameThenReturnMatchingItems() throws Exception {
        try (var tracker = new HbmTracker()) {
            Item item = new Item("searchable");
            tracker.add(item);
            List<Item> result = tracker.findByName("searchable");
            assertThat(result).contains(item);
        }
    }

    @Test
    public void whenFindByIdThenReturnCorrectItem() throws Exception {
        try (var tracker = new HbmTracker()) {
            Item item = new Item("byId");
            tracker.add(item);
            Item found = tracker.findById(item.getId());
            assertThat(found).isEqualTo(item);
        }
    }
}
