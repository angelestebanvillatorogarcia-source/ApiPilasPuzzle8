package ito.ed.puzzle8;

import ito.ed.excepciones.ExcepcionDeDesbordamientoDePila;
import ito.ed.interfaces.Pila;

import java.awt.Font;
import java.util.function.Supplier;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/** Juego del Puzzle8 con ventanas JOptionPane, con deshacer (undo) y rehacer (redo). */
public class JuegoConsola {

    private static final String TITULO = "Puzzle 8";
    private static final String[] OPCIONES = {
            "Mover ficha", "Deshacer", "Rehacer", "Nuevo aleatorio", "Reiniciar", "Salir"
    };

    private final Supplier<Pila<int[][]>> fabricaPilas;
    private Puzzle8 juego;
    private HistorialJuego historial;

    public JuegoConsola(Supplier<Pila<int[][]>> fabricaPilas) {
        this.fabricaPilas = fabricaPilas;
        nuevoJuego(new Puzzle8());
    }

    private void nuevoJuego(Puzzle8 nuevo) {
        juego = nuevo;
        historial = new HistorialJuego(juego, fabricaPilas.get(), fabricaPilas.get());
    }

    /** Texto con letra de ancho fijo para que el tablero se vea alineado. */
    private JTextArea crearTexto(String extra) {
        String texto = "Meta:\n 1  2  3\n 8  _  4\n 7  6  5\n\n"
                + "Tablero actual:\n" + juego
                + "\nDeshacer: " + historial.pasosUndo()
                + "  |  Rehacer: " + historial.pasosRedo()
                + extra;
        JTextArea area = new JTextArea(texto);
        area.setEditable(false);
        area.setOpaque(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        return area;
    }

    private void aviso(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, TITULO, JOptionPane.INFORMATION_MESSAGE);
    }

    public void jugar() {
        try {
            while (true) {
                int opcion = JOptionPane.showOptionDialog(null, crearTexto(""), TITULO,
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, OPCIONES, OPCIONES[0]);

                switch (opcion) {
                    case 0:
                        moverFicha();
                        break;
                    case 1:
                        if (!historial.deshacer()) {
                            aviso("Ya no hay estados previos.");
                        }
                        break;
                    case 2:
                        if (!historial.rehacer()) {
                            aviso("No hay estados hacia adelante.");
                        }
                        break;
                    case 3:
                        nuevoJuego(Puzzle8.aleatorio());
                        break;
                    case 4:
                        nuevoJuego(new Puzzle8());
                        break;
                    default:
                        aviso("Juego terminado por decision del jugador.");
                        return;
                }

                if (juego.haTerminado()) {
                    String extra = "\n\nFelicidades, lo resolviste en "
                            + historial.pasosUndo() + " movimientos.";
                    JOptionPane.showMessageDialog(null, crearTexto(extra), TITULO,
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
        } catch (ExcepcionDeDesbordamientoDePila e) {
            JOptionPane.showMessageDialog(null,
                    "La pila se ha desbordado y no puedes continuar jugando.",
                    TITULO, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void moverFicha() throws ExcepcionDeDesbordamientoDePila {
        String texto = JOptionPane.showInputDialog(null,
                "Numero de la ficha a mover (1 al 8):", TITULO, JOptionPane.QUESTION_MESSAGE);
        if (texto == null) {
            return;
        }
        int ficha;
        try {
            ficha = Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            aviso("Escribe un numero del 1 al 8.");
            return;
        }
        int movimiento = juego.movimientoParaNumero(ficha);
        if (movimiento == 0) {
            aviso("Esa ficha no existe o no esta junto al hueco.");
            return;
        }
        historial.mover(movimiento);
    }
}