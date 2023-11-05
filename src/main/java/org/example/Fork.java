package org.example;

public class Fork {
    private  int forkNumber;

    public Fork(int forkNumber) {
        this.forkNumber = forkNumber;
    }

    @Override
    public String toString() {
        return "forkNumber: " + forkNumber ;
    }
}
