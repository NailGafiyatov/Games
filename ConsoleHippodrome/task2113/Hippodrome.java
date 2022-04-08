package com.javarush.task.task21.task2113;

import java.util.ArrayList;
import java.util.List;

public class Hippodrome {
    private List<Horse> horses;
    static Hippodrome game;

    public List<Horse> getHorses() {
        return horses;
    }

    public Hippodrome(List<Horse> horses) {
        this.horses = horses;
    }

    public static void main(String[] args) throws InterruptedException {
        Horse horse1 = new Horse("SPIRIT", 3, 0);
        Horse horse2 = new Horse("SYUZI", 3, 0);
        Horse horse3 = new Horse("WOODY", 3, 0);
        ArrayList<Horse> horses = new ArrayList<>();
        horses.add(horse1);
        horses.add(horse2);
        horses.add(horse3);
        game = new Hippodrome(horses);
        game.run();
        game.printWinner();
    }

    public void run() throws InterruptedException {
        for (int i = 1; i <= 100; i++) {
            move();
            print();
            Thread.sleep(200);
        }

    }

    public void move() {
        for (Horse horse : horses) {horse.move();}
    }

    public void print() {
        for (Horse horse : horses) {horse.print();}
        for (int i = 0; i < 10;i++) {System.out.println();}
    }

    public Horse getWinner() {
        double winnerDistance = 0;
        Horse winner = null;
        for (int i = 0; i < horses.size(); i++) {
            if (horses.get(i).distance > winnerDistance) {
                winnerDistance = horses.get(i).distance;
                winner = horses.get(i);
            }
        }
        return winner;
    }

    public void printWinner() {
        System.out.printf("Winner is %s!", getWinner().getName());
    }
}
