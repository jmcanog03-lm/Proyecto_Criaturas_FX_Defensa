package com.example.Modelo.Efectos;

import java.util.List;

import com.example.Modelo.Jugador;
import com.example.Modelo.Partida;
import com.example.Modelo.Efectos.Efecto;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * @author Emilio
 * @author Fabricio
 * @author JoseManuel
 * @version 1.0
 * @since 1.0
 */

public class NuevaHabilidad implements Efecto {

    /**
     * comportamiento del ataque Habilidad
     */

    @Override

    public void estrategia(Partida partida, TextArea txt) {

        //List<Jugador> lista = partida.getEnemigos();

        if (partida.getJugador().getVida() % 2 == 0) {
            if (partida.getEnemigos().get(0).getVida() > 0) {
                partida.getEnemigos().get(0).recibirDano( 50 * partida.getJugador().getDanoMultiplicador());
                partida.getJugador().anadirGolpes(2);
                System.out.println();
                Platform.runLater(() -> txt
                        .appendText(
                                "Has hecho la mitad de daño a: " + partida.getEnemigos().get(0).getNombre() + "\n"));
                System.out.println();
            }else{
                 Platform.runLater(() -> txt
                        .appendText(
                                "Has hecho la mitad de daño a: " + partida.getEnemigos().get(0).getNombre() + "\n"));
                System.out.println();
            }

        }

    }

}
