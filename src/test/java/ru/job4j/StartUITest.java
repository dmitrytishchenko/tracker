package ru.job4j;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.job4j.tracker.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.function.Consumer;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StartUITest {

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final Consumer<String> output = new Consumer<String>() {
        private final PrintStream stdout = new PrintStream(out);
        @Override
        public void accept(String s) {
            stdout.println(s);
        }
    };

    @Before
    public void loadOutput() {
        output.accept("execute before method");
        System.setOut(new PrintStream(this.out));
        Tracker tracker = new Tracker();
        Item item = tracker.add(new Item("name", "desc"));
        Input input = new StubInput(new String[]{"0", "test name", "desc", "6"});
        MenuTracker menu = new MenuTracker(input, tracker, output);
        new StartUI(input, tracker, output).init();
    }
    @After
    public void backOutput() {
        System.setOut(new PrintStream(out));
        output.accept("execute after method");
    }
    @Test
    public void whenUpdateThenTrackerHasUpdateValue2() {
        Tracker tracker = new Tracker();
        Input input = new StubInput(new String[]{"0", "test name", "desc", "6"});
        MenuTracker menu = new MenuTracker(input, tracker, output);
        Item item = tracker.add(new Item("test name", "desc"));
        assertThat(tracker.findById(item.getId()).getName(), is("test name"));
    }
    @Test
    public  void whenUserRequestAllItems2() {
        Tracker tracker = new Tracker();
        Item item = tracker.add(new Item("test name", "desc"));
        assertThat(tracker.findAll().get(0), is(item));
    }
    @Test
    public void whenUserReplaceItem2() {
        Tracker tracker = new Tracker();
        Item item = tracker.add(new Item("test name", "desc"));
        assertThat(tracker.findById(item.getId()).getName(), is("test name"));
    }

    @Test
    public void whenUserFindItemByName2() {
        Tracker tracker = new Tracker();
        Item item = tracker.add(new Item("name", "desc"));
        assertThat(tracker.findByName(item.getName()).get(0), is(item));
    }
    @Test
    public void whenUserFindItemById2() {
        Tracker tracker = new Tracker();
        Item item = tracker.add(new Item("name", "desc"));
        assertThat(tracker.findById(item.getId()), is(item));
    }
}
