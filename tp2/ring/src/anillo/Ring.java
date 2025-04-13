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

    abstract class Link {
        abstract Link add(Object cargo);
        abstract Link remove();
        abstract Link next();
        abstract Object current();
    }

    class EmptyLink extends Link {
        @Override
        Link add(Object cargo) {
            MultiLink newLink = new MultiLink(cargo);
            newLink.next = newLink;
            actions.push(new NullFunction());
            return newLink;
        }

        @Override
        Link remove() {
            throw new RuntimeException("Ring is empty");
        }

        @Override
        Link next() {
            throw new RuntimeException("Ring is empty");
        }

        @Override
        Object current() {
            throw new RuntimeException("Ring is empty");
        }
    }

    class MultiLink extends Link {
        private final Object cargo;
        Link next;

        MultiLink(Object cargo) {
            this.cargo = cargo;
        }

        @Override
        Link add(Object newCargo) {
            MultiLink newLink = new MultiLink(newCargo);
            MultiLink prev = this;

            while (prev.next != this) {
                prev = (MultiLink) prev.next;
            }

            newNode.next = this;
            prev.next = newNode;

            actions.push(new RegularFunction());
            return newNode;
        }

        @Override
        Link remove() {
            LinkFunc func = actions.pop();
            return func.apply(this);
        }

        @Override
        Link next() {
            return next;
        }

        @Override
        Object current() {
            return cargo;
        }
    }

    abstract class LinkFunc {
        abstract Link apply(Link link);
    }

    class RegularFunction extends LinkFunc {
        @Override
        Link apply(Link link) {
            MultiLink mn = (MultiLink) link;
            MultiLink prev = mn;

            while (prev.next != mn) {
                prev = (MultiLink) prev.next;
            }

            prev.next = mn.next;
            return mn.next;
        }
    }

    class NullFunction extends LinkFunc {
        @Override
        Link apply(Link link) {
            return new EmptyLink();
        }
    }
}
