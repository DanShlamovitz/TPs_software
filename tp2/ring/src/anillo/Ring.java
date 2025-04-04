package anillo;
import java.util.ArrayList;

public class Ring {

    private ArrayList<Object> ring;

    // next tiene que avanzar el current y devolver el ring resultante
    public Ring next() {
        if (ring.size() <= 1) {
            // NO ME DEJA DEVOLVER EXCEPTION PORQUE EL TIPO DE LA FUNCION ES Ring! Entonces devuelvo el mismo ring
            //return new Exception("The ring doesn't have enough elements for there to be a next element.");
            return this;
        }
        Object curr = ring.removeFirst();
        ring.add(ring.size() - 1, curr);
        return this;
    }

    public Object current() {
        if (ring.isEmpty()) {
            return new Exception("The ring is empty, there is no current element.");
        }
        return ring.getFirst();
    }


    public Ring add( Object cargo ) {
        if (!ring.isEmpty()) {
            Object curr = ring.removeFirst(); // saco el current
            ring.addFirst(cargo); // agrego el nuevo
            ring.addFirst(curr); // vuelvo a poner el current
        }
        else {
            ring.addFirst(cargo);
        }
        return this;
    }

    public Ring remove() {
        if (ring.isEmpty()) {
            // nuevamente no puedo devolver una excepcion porque el tipo de la funcion es Ring, no Object.
            return this;
        }
        ring.removeFirst();
        return this;
    }
}
