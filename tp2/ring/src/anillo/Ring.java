package anillo;

import java.util.LinkedList;

public class Ring {

    private LinkedList<Object> ring = new LinkedList<>();

    public Ring next() {
        if (ring.isEmpty()) {
            throw new RuntimeException("The ring is empty");
        }
        Object curr = ring.removeFirst();
        ring.addLast(curr);
        return this;
    }

    public Object current() {
        if (ring.isEmpty()) {
            throw new RuntimeException("The ring is empty");
        }
        return ring.getFirst();
    }

    public Ring add(Object cargo) {
        ring.addFirst(cargo);
        return this;
    }

    public Ring remove() {
        if (!ring.isEmpty()) {
            ring.removeFirst();
        }
        return this;
    }
}
