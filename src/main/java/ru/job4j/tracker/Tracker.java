package ru.job4j.tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Tracker implements ITracker {
    private List<Item> items = new ArrayList<>();

    private static final Random RANDOM = new Random();

    public Item add(Item item) {
        item.setId(this.generateId());
        this.items.add(item);
        return item;
    }

    public boolean replace(String id, Item item) {
        boolean repl = false;
        for (Item item1 : this.items) {
            int value = 0;
            if (item1 != null && item1.getId().equals(id)) {
                this.items.set(value, item);
                repl = true;
                break;
            }
            value++;
        }
        return repl;
    }

    public boolean delete(String id) {
        boolean result = false;
        for (int i = 0; i < this.items.size(); i++) {
            if (this.items.get(i).getId().equals(id)) {
                this.items.remove(i);
                result = true;
                break;
            }
        }
        return result;
    }

    public List<Item> findAll() {
        return this.items;
    }

    public List<Item> findByName(String key) {
        List<Item> result = this.items.stream()
                .filter(n -> n.getName().equals(key))
                .collect(Collectors.toList());
        return result;
    }

    public Item findById(String id) {
        Item result = this.items.stream()
                .filter(n -> n.getId().equals(id))
                .findFirst().orElse(null);
        return result;
    }

    protected String generateId() {
        return String.valueOf(System.currentTimeMillis() + RANDOM.nextInt());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tracker tracker = (Tracker) o;
        return items.equals(tracker.items);
    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }

    public enum TrackerSingleEnum {
        INSTANCE;

        public Item add(Item model) {
            return model;
        }
    }

    public static class TrackerSingle {
        private static TrackerSingle instance;
        private static final TrackerSingle INSTANCE = new TrackerSingle();

        private TrackerSingle() {
        }

        public static TrackerSingle getInstance() {
            if (instance == null) {
                instance = new TrackerSingle();
            }
            return instance;
        }

        public static TrackerSingle getInstance2() {
            return INSTANCE;
        }

        public static TrackerSingle getInstance3() {
            return Holder.INSTANCE;
        }

        public Item add(Item model) {
            return model;
        }

        private static final class Holder {
            private static final TrackerSingle INSTANCE = new TrackerSingle();
        }
    }
}