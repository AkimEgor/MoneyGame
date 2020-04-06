package ru.yandex.chechin.rugball.RelizActivity;

public class MessageArrayAdapter {
    public String name;
    int height;
    String nameMagaz;
    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }
    public String getName1() {
        return nameMagaz;
    }
    public MessageArrayAdapter(String name, int height, String name1) {
        this.name = name;
        this.height = height;
        this.nameMagaz = name1;
    }

}

