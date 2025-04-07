package anillo;

abstract class Node {
    public abstract Node add(Object cargo);
    public abstract Node remove();
    public abstract Node next();
    public abstract Object current();
}

class EmptyNode extends Node {
    public Node add(Object cargo) {
        FullNode newNode = new FullNode(cargo);
        newNode.next = newNode;
        newNode.prev = newNode;
        return newNode;
    }

    public Node remove() {
        throw new RuntimeException("The ring is empty");
    }

    public Node next() {
        throw new RuntimeException("The ring is empty");
    }

    public Object current() {
        throw new RuntimeException("The ring is empty");
    }
}

class FullNode extends Node {
    Object cargo;
    FullNode next;
    FullNode prev;

    public FullNode(Object cargo) {
        this.cargo = cargo;
    }

    public Node add(Object newCargo) {
        FullNode newNode = new FullNode(newCargo);
        newNode.next = this;
        newNode.prev = this.prev;
        this.prev.next = newNode;
        this.prev = newNode;
        return newNode;
    }
    public Node remove() {
        if (this.next == this) {
            return new EmptyNode();
        }
        this.prev.next = this.next;
        this.next.prev = this.prev;
        return this.next;
    }

    public Node next() {
        return this.next;
    }

    public Object current() {
        return this.cargo;
    }
}

public class Ring {
    private Node current = new EmptyNode();

    public Ring next() {
        current = current.next();
        return this;
    }

    public Object current() {
        return current.current();
    }

    public Ring add(Object cargo) {
        current = current.add(cargo);
        return this;
    }

    public Ring remove() {
        current = current.remove();
        return this;
    }
}
