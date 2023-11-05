package org.example;

import java.util.HashMap;
import java.util.Map;



public class ForkService {
    private final Map<Integer, Fork> forks;
    public ForkService() {
        this.forks = new HashMap<>();
    }

    public Map<Integer, Fork> getFork(int numberLeftFork, int numberRightFork) throws InterruptedException {
        Map <Integer, Fork> result = new HashMap<>();
        synchronized (this){
            if (forks.get(numberLeftFork) == null || forks.get(numberRightFork)== null){
                wait();
            } else {
                result.put(numberLeftFork, forks.get(numberLeftFork));
                result.put(numberRightFork, forks.get(numberRightFork));
                forks.put(numberLeftFork, null);
                forks.put(numberRightFork, null);
            }
            return result;
        }
    }

    public void putFork(int number, Fork fork) throws InterruptedException {
        synchronized (this){
            forks.put(number, fork);
            notify();
        }
    }

    public void addFork() throws InterruptedException {
        System.out.println("Сервируем стол...");
        Thread.sleep(300);
        for (int i = 1; i <= 5; i++) {
            forks.put(i, new Fork(i));
        }
    }

}
