package ito.ed.puzzle8;

import ito.ed.excepciones.ExcepcionDeDesbordamientoDePila;
import ito.ed.excepciones.ExcepcionDePilaVacia;
import ito.ed.interfaces.Pila;

/**
 * Controla deshacer (undo) y rehacer (redo) con dos pilas. Solo conoce la
 * INTERFAZ Pila, asi funciona igual con PilaEstatica o PilaDinamica (polimorfismo).
 */
public class HistorialJuego {
    private final JuegoDeTablero juego;
    private final Pila<int[][]> pilaUndo;
    private final Pila<int[][]> pilaRedo;

    public HistorialJuego(JuegoDeTablero juego, Pila<int[][]> pilaUndo, Pila<int[][]> pilaRedo) {
        this.juego = juego;
        this.pilaUndo = pilaUndo;
        this.pilaRedo = pilaRedo;
    }

    /** Guarda el estado previo en la pila undo y aplica el movimiento. Devuelve false si no era valido. */
    public boolean mover(int movimiento) throws ExcepcionDeDesbordamientoDePila {
        if (!juego.puedeMover(movimiento)) {
            return false;
        }
        pilaUndo.push(juego.estadoActual());
        juego.cambiaDeEstado(movimiento);
        vaciarRedo();   // un movimiento nuevo invalida los estados "hacia adelante"
        return true;
    }

    /** Devuelve false si ya no hay estados previos. */
    public boolean deshacer() throws ExcepcionDeDesbordamientoDePila {
        if (pilaUndo.isEmpty()) {
            return false;
        }
        try {
            pilaRedo.push(juego.estadoActual());
            juego.restaurar(pilaUndo.pop());
            return true;
        } catch (ExcepcionDePilaVacia e) {
            return false;
        }
    }

    /** Devuelve false si no hay estados hacia adelante. */
    public boolean rehacer() throws ExcepcionDeDesbordamientoDePila {
        if (pilaRedo.isEmpty()) {
            return false;
        }
        try {
            pilaUndo.push(juego.estadoActual());
            juego.restaurar(pilaRedo.pop());
            return true;
        } catch (ExcepcionDePilaVacia e) {
            return false;
        }
    }

    private void vaciarRedo() {
        try {
            while (!pilaRedo.isEmpty()) {
                pilaRedo.pop();
            }
        } catch (ExcepcionDePilaVacia e) {
            // no ocurre: se revisa isEmpty() antes de cada pop()
        }
    }

    public int pasosUndo() {
        return pilaUndo.length();
    }

    public int pasosRedo() {
        return pilaRedo.length();
    }
}
