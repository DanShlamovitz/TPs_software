package anillo;

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
