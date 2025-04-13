package anillo;

import java.util.Stack;

public class Ring {
    Link current = new EmptyLink();
    static final Stack<LinkFunc> actions = new Stack<>();

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