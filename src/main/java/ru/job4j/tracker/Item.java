package ru.job4j.tracker;

import java.util.Objects;

public class Item {
    private String id;
    private String name;
    private String desc;
    private Long create;
    private String comments;

    public Item(String name, String desc, Long create, String comments) {
        this.name = name;
        this.desc = desc;
        this.create = create;
        this.comments = comments;
    }

    public Item(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public Item() {
    }

    public Item(String id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return this.name;
    }

    public String getDesc() {
        return this.desc;
    }

    public Long getCreate() {
        return this.create;
    }

    public String getComments() {
        return this.comments;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return Objects.equals(id, item.id)
                && Objects.equals(name, item.name)
                && Objects.equals(desc, item.desc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, desc);
    }
}
