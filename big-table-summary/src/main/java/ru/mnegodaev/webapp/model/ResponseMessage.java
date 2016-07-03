package ru.mnegodaev.webapp.model;

public class ResponseMessage {
    private String name;
    private String text;

    public ResponseMessage(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }
}
