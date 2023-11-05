package org.example;

import java.util.Map;
import java.util.concurrent.Semaphore;
import static java.lang.Thread.sleep;

public class Philosopher implements Runnable {
    private String name;
    private int countEating;
    private Fork leftFork;
    private Fork rightFork;
    private int leftForkNumber;
    private int rightForkNumber;
    private ForkService forkService;
    private Semaphore semaphore = new Semaphore(2, true);

    public Philosopher(String name, int leftForkNumber, int rightForkNumber, ForkService forkService) {
        this.name = name;
        this.countEating = 0;
        this.leftFork = null;
        this.rightFork = null;
        this.leftForkNumber = leftForkNumber;
        this.rightForkNumber = rightForkNumber;
        this.forkService = forkService;

    }

    public void eating() throws InterruptedException {
        System.out.println(name + " начал есть.");
        sleep(1000);
        System.out.println(name + " закончил есть есть.");
        countEating++;
        System.out.println(name + " поел: " + countEating + " раз/а." );
    }

    public void think() throws InterruptedException {
        System.out.println(name + " размышляет.");
        sleep(1000);
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            while (countEating < 3) {
               Map<Integer, Fork> forks = forkService.getFork(leftForkNumber,rightForkNumber);
                leftFork = forks.get(leftForkNumber);
                if (leftFork != null)
                    System.out.println(name + " взял вилку с лева!");
                rightFork = forks.get(rightForkNumber);
                if (rightFork != null)
                    System.out.println(name + " взял вилку с права!");
                if (leftFork != null && rightFork != null) {
                    eating();
                    forkService.putFork(rightForkNumber, rightFork);
                    forkService.putFork(leftForkNumber, leftFork);
                    think();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}


