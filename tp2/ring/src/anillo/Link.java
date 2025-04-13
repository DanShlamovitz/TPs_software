package anillo;

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
        Ring.actions.push(new NullFunction());
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
        MultiLink newNode = new MultiLink(newCargo);
        MultiLink prev = this;

        while (prev.next != this) {
            prev = (MultiLink) prev.next;
        }

        newNode.next = this;
        prev.next = newNode;

        Ring.actions.push(new RegularFunction());
        return newNode;
    }

    @Override
    Link remove() {
        LinkFunc func = Ring.actions.pop();
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
