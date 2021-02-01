package ru.job4j.tracker;

import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.apache.logging.log4j.LogManager.getLogger;


public class TrackerSQL implements ITracker, AutoCloseable {
    private Connection connection;
    private static final Logger LOG = getLogger(TrackerSQL.class);

    public TrackerSQL(Connection connection) {
        this.connection = connection;
    }

    public TrackerSQL() {
        this.init();
    }

    public boolean init() {
        try (InputStream in = TrackerSQL.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            assert in != null;
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            this.connection = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
            this.createTable();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return this.connection != null;
    }

    public boolean createTable() {
        boolean result = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement("create table  if not exists items"
                + "(id varchar(100) primary key,"
                + "name varchar (100),"
                + "description varchar (100))")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Item add(Item item) {
        Item result = null;
        String sql = "INSERT INTO items(id, name, description) VALUES(?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, item.getId());
            ps.setString(2, item.getName());
            ps.setString(3, item.getDesc());
            ps.executeUpdate();
            result = item;
        } catch (SQLException e) {
            LOG.error("Error occured in creating item", e);
        }
        return result;
    }

    @Override
    public boolean replace(String id, Item item) {
        boolean result = false;
        String sql = "UPDATE items AS i SET name = ?, description = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, item.getName());
            ps.setString(2, item.getDesc());
            ps.setString(3, id);
            int value = ps.executeUpdate();
            if (value > 0) {
                result = true;
            }
        } catch (SQLException e) {
            LOG.error("Error replaced the item", e);
        }
        return result;
    }

    @Override
    public boolean delete(String id) {
        boolean result = false;
        String sql = "DELETE FROM items WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            int value = ps.executeUpdate();
            if (value > 0) {
                result = true;
            }
        } catch (SQLException e) {
            LOG.error("Error delete the item", e);
        }
        return result;
    }

    @Override
    public List<Item> findAll() {
        List<Item> list = new ArrayList<>();
        String sql = "SELECT * FROM items";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Item(rs.getString("name"), rs.getString("description")));
            }
        } catch (SQLException e) {
            LOG.error("Error find the items", e);
        }
        return list;
    }

    @Override
    public List<Item> findByName(String key) {
        List<Item> list = new ArrayList<>();
        String sql = "SELECT * FROM items WHERE name = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, key);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Item(rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("description")));
            }
        } catch (SQLException e) {
            LOG.error("Error find the item", e);
        }
        return list;
    }

    @Override
    public Item findById(String id) {
        Item result = null;
        String sql = "SELECT  * FROM items WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result = new Item(rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("description"));
            }
        } catch (SQLException e) {
            LOG.error("Error find the item", e);
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        if (this.connection != null) {
            connection.close();
        }
    }
}
