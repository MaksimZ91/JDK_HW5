package org.example;


public class Main {
    public static void main(String[] args) {
        Thread table = new Thread(new Table());
        table.start();
    }
}