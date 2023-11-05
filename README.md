# Урок 5. Многопоточность  
Пять безмолвных философов сидят вокруг круглого стола, перед каждым философом стоит тарелка спагетти.  
Вилки лежат на столе между каждой парой ближайших философов.  
Каждый философ может либо есть, либо размышлять.  
Философ может есть только тогда, когда держит две вилки — взятую справа и слева.  
Философ не может есть два раза подряд, не прервавшись на размышления (можно не учитывать)  
Философ может взять только две вилки сразу, то есть обе вилки должны быть свободны  
## Class Main
```java
package org.example;


public class Main {
    public static void main(String[] args) {
        Thread table = new Thread(new Table());
        table.start();
    }
}
```

## Class Table
```java
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
```
## Class Philosopher
```java
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
```
## Class ForkService
```java
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
```

