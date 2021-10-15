package fr.uge.ex3;

import java.util.ArrayList;

public class ThreadSafeList {
    private final ArrayList<Integer> encapList = new ArrayList<>();

    public void add(Integer i) {
        synchronized (encapList) {
            encapList.add(i);
        }
    }

    public int size() {
        synchronized (encapList) {
            return encapList.size();
        }
    }
}
