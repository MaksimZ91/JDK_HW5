package org.example;

import java.util.ArrayList;
import java.util.List;


public class Table implements Runnable {

    private ForkService forkService = new ForkService();
    Thread cant = new Thread(new Philosopher("Кант",1,5, forkService));
    Thread  demokrit = new Thread(new Philosopher("Демокрит",2,1, forkService));
    Thread  confucii = new Thread(new Philosopher("Конфуций",3,2, forkService));
    Thread  csenofont = new Thread(new Philosopher("Ксенофонт",4,3,forkService));
    Thread  platon = new Thread(new Philosopher("Платон",5,4,forkService));
    List <Thread> philosophers = new ArrayList<>();

    @Override
    public void run() {
        try {
            forkService.addFork();
            philosophers.add(cant);
            philosophers.add(demokrit);
            philosophers.add(confucii);
            philosophers.add(csenofont);
            philosophers.add(platon);
            lunch();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

     public void lunch(){
         for (Thread t: philosophers ) {
             t.start();
         }


     }
}
