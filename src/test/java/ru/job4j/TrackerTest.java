package ru.job4j;


import org.junit.Test;
import ru.job4j.tracker.Item;
import ru.job4j.tracker.Tracker;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TrackerTest {


    @Test
    public void add() {
        Tracker tracker = new Tracker();
        Item item = new Item("test1", "testDescription1", 123L, "first item");
        Item result = tracker.add(item);
        assertThat(result, is(item));
    }

    @Test
    public void replace() {
        Tracker tracker = new Tracker();
        Item item = new Item("test2", "testDescription2", 124L, "second item");
        tracker.add(item);
        Boolean result = tracker.replace(item.getId(), item);
        assertThat(result, is(true));
    }

    @Test
    public void findById() {
        Tracker tracker = new Tracker();
        Item item = new Item("test3", "testDescrition3", 125L, "third item");
        tracker.add(item);
        Item result = tracker.findById(item.getId());
        assertThat(result, is(item));
    }

    @Test
    public void delete() {
        Tracker tracker = new Tracker();
        Item item = new Item();
        tracker.add(item);
        boolean result = tracker.delete(item.getId());
        assertThat(result, is(true));
    }

    @Test
    public void findAll() {
        Tracker tracker = new Tracker();
        Item item = new Item("test5", "testDescription5", 127L, "fifth item");
        tracker.add(item);
        List<Item> result = tracker.findAll();
        assertThat(result.get(0), is(item));
    }

    @Test
    public void findByName() {
        Tracker tracker = new Tracker();
        Item item = new Item("test6", "testDescription6", 128L, "sixth item");
        tracker.add(item);
        List<Item> result = tracker.findByName("test6");
        assertThat(result.get(0), is(item));
    }
}