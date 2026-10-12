package ito.ed.puzzle8;

import ito.ed.interfaces.Pila;
import ito.ed.interfaces.pila.PilaDinamica;
import ito.ed.interfaces.pila.PilaEstatica;

import java.util.function.Supplier;

import javax.swing.JOptionPane;

public class Main {
    /** Capacidad de las pilas estaticas (si se llena, el juego termina). */
    private static final int CAPACIDAD = 50;

    public static void main(String[] args) {
        Object[] opciones = {"Estatica", "Dinamica"};
        int seleccion = JOptionPane.showOptionDialog(null,
                "Que implementacion de pila quieres usar?", "Puzzle 8",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, opciones, opciones[0]);

        if (seleccion >= 0) {
            // Mismo tipo (Pila<int[][]>), distinta implementacion: polimorfismo
            Supplier<Pila<int[][]>> fabrica;
            if (seleccion == 1) {
                fabrica = () -> new PilaDinamica<int[][]>();
            } else {
                fabrica = () -> new PilaEstatica<int[][]>(CAPACIDAD);
            }
            new JuegoConsola(fabrica).jugar();
        }
        System.exit(0);
    }
}