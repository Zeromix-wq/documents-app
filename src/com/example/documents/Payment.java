package com.example.documents;

import java.time.LocalDate;

public class Payment extends Document {
    private double amount;
    private String employee;

    public Payment(String number, LocalDate date, String user, 
                   double amount, String employee) {
        super(number, date, user);
        this.amount = amount;
        this.employee = employee;
    }

    @Override
    public String getTypeName() { return "Платёжка"; }

    @Override
    public String getDisplayText() {
        return String.format("Платёжка от %s номер %s", formatDate(), number);
    }

    @Override
    public String getDetails() {
        return String.format(
            "Номер: %s\nДата: %s\nПользователь: %s\n" +
            "Сумма: %.2f\nСотрудник: %s",
            number, formatDate(), user, amount, employee
        );
    }

    public double getAmount() { return amount; }
    public String getEmployee() { return employee; }
}