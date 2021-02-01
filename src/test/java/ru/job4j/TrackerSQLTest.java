package ru.job4j;

import org.junit.Test;
import ru.job4j.tracker.ConnectionRollback;
import ru.job4j.tracker.Item;
import ru.job4j.tracker.TrackerSQL;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class TrackerSQLTest {

    public Connection init() {
        try (InputStream in = TrackerSQL.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            return DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void createItem() throws Exception {
        try (TrackerSQL tracker = new TrackerSQL(ConnectionRollback.create(this.init()))) {
            tracker.createTable();
            tracker.add(new Item("1", "name", "desc"));
            assertThat(tracker.findByName("name").size(), is(1));
        }
    }

    @Test
    public void replaceItem() throws Exception {
        try (TrackerSQL trackerSQL = new TrackerSQL(ConnectionRollback.create(this.init()))) {
            trackerSQL.createTable();
            trackerSQL.add(new Item("1", "name", "desc"));
            assertThat(trackerSQL.replace("1", new Item("2", "name2", "desc2")), is(true));
        }
    }

    @Test
    public void deleteItem() throws Exception {
        try (TrackerSQL trackerSQL = new TrackerSQL(ConnectionRollback.create(this.init()))) {
            trackerSQL.createTable();
            trackerSQL.add(new Item("1", "name", "desc"));
            assertThat(trackerSQL.delete("1"), is(true));
        }
    }

    @Test
    public void findAllItem() throws Exception {
        try (TrackerSQL trackerSQL = new TrackerSQL(ConnectionRollback.create(this.init()))) {
            trackerSQL.createTable();
            trackerSQL.add(new Item("1", "name", "desc"));
            trackerSQL.add(new Item("2", "name1", "desc1"));
            assertThat(trackerSQL.findAll().size(), is(2));
        }
    }

    @Test
    public void findItemByName() throws Exception {
        try (TrackerSQL trackerSQL = new TrackerSQL(ConnectionRollback.create(this.init()))) {
            trackerSQL.createTable();
            Item item = new Item("1", "name", "desc");
            trackerSQL.add(item);
            assertThat(trackerSQL.findByName("name").size(), is(1));
        }
    }

    @Test
    public void findItemById() throws Exception {
        try (TrackerSQL trackerSQL = new TrackerSQL(ConnectionRollback.create(this.init()))) {
            trackerSQL.createTable();
            Item item = new Item("1", "name", "desc");
            trackerSQL.add(item);
            assertThat(trackerSQL.findById(item.getId()), is(item));
        }
    }
}