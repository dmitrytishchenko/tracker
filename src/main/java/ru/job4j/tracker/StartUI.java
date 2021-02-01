package ru.job4j.tracker;

import java.util.function.Consumer;

public class StartUI {
    /**
     * Получение данных от пользователя.
     */
    private final Input input;
    /**
     * Хранилище заявок
     */
    private final Tracker tracker;
    private final Consumer<String> output;
    private boolean work = true;
    /**
     * Конструктор инициализирующий поля
     * @param input ввод данных.
     * @param tracker хранилище заявок.
     */
    public StartUI(Input input, Tracker tracker, Consumer<String> output) {
        this.input = input;
        this.tracker = tracker;
        this.output = output;
    }
    /**
     * Основной цикл программы.
     */
    public void init() {
        Tracker tracker = new Tracker();
        MenuTracker menu = new MenuTracker(this.input, this.tracker, output);
        menu.fillactions(this);
        do {
            for (int i = 0; i < 100000; i++) {
                menu.show();
            }
            menu.select(input.ask("Select: ", menu.ranges()));
        }
        while (this.work);
    }
    public void stop() {
        this.work = false;
    }

    /**
     * Запуск программы.
     * @param args
     */
    public static void main(String[] args) {
        new StartUI(new ValidateInput(), new Tracker(), System.out::println).init();
    }
}