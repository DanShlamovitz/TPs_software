package anillo;

import java.util.LinkedList;

abstract class Node{
    public Node next;
    public Node prev;
    public Object cargo;

    public abstract Node add(Object cargo);
    public abstract Node remove();
    public abstract Node next();
    public abstract Object current();

}

class EmptyNode extends Node {
    public EmptyNode() {

    }
}

class NodeWithSomething extends Node {
    public NodeWithSomething(Object cargo) {}
}


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
