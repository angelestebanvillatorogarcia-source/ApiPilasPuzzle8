package ito.ed.puzzle8;

import java.util.Arrays;
import java.util.Random;

/** Tablero de 3x3 con numeros del 1 al 8 y un hueco (0). */
public class Puzzle8 extends JuegoDeTablero {

    // Movimientos del HUECO
    public static final int ARRIBA = 1;
    public static final int ABAJO = 2;
    public static final int IZQUIERDA = 3;
    public static final int DERECHA = 4;

    private static final int[][] META = {
            {1, 2, 3},
            {8, 0, 4},
            {7, 6, 5}
    };

    private int[][] puzle8;
    private int x, y;   // fila y columna donde esta el hueco (0)

    /** Siempre inicia con el mismo tablero (se resuelve en 5 movimientos). */
    public Puzzle8() {
        cargar(new int[][]{
                {2, 8, 3},
                {1, 6, 4},
                {7, 0, 5}
        });
    }
    public Puzzle8(int[][] puzle8) {
        cargar(puzle8);
    }
    /** Tablero aleatorio: se mezcla desde la meta con movimientos validos (siempre tiene solucion). */
    public static Puzzle8 aleatorio() {
        Random azar = new Random();
        Puzzle8 p = new Puzzle8(META);
        do {
            for (int i = 0; i < 40; i++) {
                int mov = azar.nextInt(4) + 1;
                if (p.puedeMover(mov)) {
                    p.cambiaDeEstado(mov);
                }
            }
        } while (p.haTerminado());
        return p;
    }
    private void cargar(int[][] origen) {
        if (origen == null || origen.length != 3) {
            throw new IllegalArgumentException("El tablero debe ser de 3x3");
        }
        boolean[] visto = new boolean[9];
        int[][] copia = new int[3][3];
        for (int i = 0; i < 3; i++) {
            if (origen[i] == null || origen[i].length != 3) {
                throw new IllegalArgumentException("El tablero debe ser de 3x3");
            }
            for (int j = 0; j < 3; j++) {
                int v = origen[i][j];
                if (v < 0 || v > 8 || visto[v]) {
                    throw new IllegalArgumentException("Debe contener los numeros 0 al 8 sin repetir");
                }
                visto[v] = true;
                copia[i][j] = v;
                if (v == 0) {
                    x = i;
                    y = j;
                }
            }
        }
        puzle8 = copia;
    }
    public int[][] estadoActual() {
        int[][] copia = new int[3][3];
        for (int i = 0; i < 3; i++) {
            copia[i] = Arrays.copyOf(puzle8[i], 3);
        }
        return copia;
    }
    public boolean haTerminado() {
        return Arrays.deepEquals(puzle8, META);
    }
    public boolean puedeMover(int movimiento) {
        switch (movimiento) {
            case ARRIBA:    return x > 0;
            case ABAJO:     return x < 2;
            case IZQUIERDA: return y > 0;
            case DERECHA:   return y < 2;
            default:        return false;
        }
    }
    /** Mueve el hueco en la direccion indicada (la ficha vecina ocupa su lugar). */
    public void cambiaDeEstado(int movimiento) {
        if (!puedeMover(movimiento)) {
            throw new IllegalArgumentException("Movimiento no valido: " + movimiento);
        }
        int nx = x;
        int ny = y;
        switch (movimiento) {
            case ARRIBA:    nx--; break;
            case ABAJO:     nx++; break;
            case IZQUIERDA: ny--; break;
            case DERECHA:   ny++; break;
            default:        break;
        }
        puzle8[x][y] = puzle8[nx][ny];
        puzle8[nx][ny] = 0;
        x = nx;
        y = ny;
    }
    public void restaurar(int[][] estado) {
        cargar(estado);
    }
    /**
     * Dada una ficha por su numero (1 al 8) devuelve el movimiento del hueco que
     * la intercambia con el, o 0 si la ficha no esta junto al hueco.
     */
    public int movimientoParaNumero(int ficha) {
        if (x > 0 && puzle8[x - 1][y] == ficha) return ARRIBA;
        if (x < 2 && puzle8[x + 1][y] == ficha) return ABAJO;
        if (y > 0 && puzle8[x][y - 1] == ficha) return IZQUIERDA;
        if (y < 2 && puzle8[x][y + 1] == ficha) return DERECHA;
        return 0;
    }
}
