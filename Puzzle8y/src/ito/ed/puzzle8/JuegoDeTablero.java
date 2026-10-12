package ito.ed.puzzle8;

/**
 * Clase abstracta: lo que debe saber hacer cualquier juego de tablero
 * cuyos estados se guardan en pilas. Puzzle8 la extiende.
 */
public abstract class JuegoDeTablero {

    /** Devuelve una COPIA del estado actual. */
    public abstract int[][] estadoActual();
    public abstract boolean haTerminado();
    public abstract boolean puedeMover(int movimiento);
    public abstract void cambiaDeEstado(int movimiento);
    /** Vuelve a un estado guardado (lo usan undo y redo). */
    public abstract void restaurar(int[][] estado);
    /** Metodo concreto compartido por las subclases. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] fila : estadoActual()) {
            for (int v : fila) {
                sb.append(v == 0 ? " _ " : " " + v + " ");
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
