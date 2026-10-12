package com.example.documents;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Document {
    protected String number;
    protected LocalDate date;
    protected String user;

    public Document(String number, LocalDate date, String user) {
        this.number = number;
        this.date = date;
        this.user = user;
    }

    public String getNumber() { return number; }
    public LocalDate getDate() { return date; }
    public String getUser() { return user; }

    public abstract String getTypeName();
    public abstract String getDisplayText();
    public abstract String getDetails();

    public String formatDate() {
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}