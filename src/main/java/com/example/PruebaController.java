package com.example;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import com.example.Modelo.Ataque;
import com.example.Modelo.Jugador;
import com.example.Modelo.ManagerJugador;
import com.example.Modelo.Partida;
import com.example.Modelo.ReaderJugador;

import java.awt.Desktop;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

 /**
     * @author Emilio
     * @author Fabricio
     * @author JoseManuel
     * @version 1.0
     * @since 1.0
     */

public class PruebaController implements Initializable {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
    @FXML
    

    final boolean[] verdad = { false };

    private Jugador jugadorGeneral;

    private ManagerJugador mj;
    private ReaderJugador rj;
    private Partida partida;

    
    /**
     * inicializar todo en el controlador
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
        
        try {
            rj = new ReaderJugador();
            mj = new ManagerJugador();

            List<Jugador> jugadores = rj.leer();

            for (Jugador jugador : jugadores) {
                mj.guardarJugador(jugador);
            }

            

        } catch (IOException e) {

            e.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }

        

        btnEmpezar.setOnAction(event -> {
            if(!partida.getJuegoAcabado())
                Empezar();
        });

        

        

       btnAtaque.setOnAction(event -> {
            if(!partida.getJuegoAcabado()){
                Ataque ataque = cmbAtaques.getValue();

            if (ataque == null) {
                Platform.runLater(() ->
                    txtArea.appendText("No has seleccionado ningún ataque!\n")
                );
                return;
            }
        
            if (jugadorGeneral.getMonocos() < ataque.getCoste()) {
                Platform.runLater(() ->
                    txtArea.appendText("No tienes suficientes Monocos!\n")
                );
                return;
            }
        

            jugadorGeneral.ejecutarAtaque(ataque, partida, txtArea);
        
            List<Jugador> enemigos = partida.getEnemigos();
        
            for (int i = 0; i < enemigos.size(); i++) {
            
                Jugador enemigo = enemigos.get(i);
            
                if (enemigo.getVida() <= 0) {
                    enemigo.setVida(0);
                

                    if (i == 0) {
                        partida.setEnemigos(
                            partida.ordenarEnemigos(enemigos)
                        );
                        break;
                    }
                }
            }

            if (partida.todosMuertos(partida.getEnemigos())) {
                partida.setJuegoAcabado(true);
    Platform.runLater(() ->
        txtArea.appendText("Has completado la expedicion horripilante any% glitchless speedrun mode, felicidades\n")
    );
}

    

        jugadorGeneral.setMonocos(jugadorGeneral.getMonocos() - ataque.getCoste());

            mostrarTextos();

            lblVida.setText(String.valueOf(jugadorGeneral.getVida()));
            lblMonocos.setText(String.valueOf(jugadorGeneral.getMonocos()));
        }
            
        });

        btnDetener.setOnAction(event -> {
            verdad[0] = true;
        });

        

    }

     
    @FXML
    private Text txtFirst;

    @FXML
    private Text txtSecond;

    @FXML
    private Text txtThird;

    @FXML
    private Text txtFourth;


    @FXML
    private Text txtHfirst;

    @FXML
    private Text txtHsecond;

    @FXML
    private Text txtHthird;

    @FXML
    private Text txtHfourth;



    @FXML
    private TextArea txtArea;

    @FXML
    private TextField txtNombre;

    @FXML
    private Button btnVolver;

    @FXML
    private Button btnEmpezar;

    @FXML
    private Button btnAtaque;

    @FXML
    private Button btnDetener;

    @FXML
    private Label lblVida;

    @FXML
    private Label lblMonocos;

    @FXML
    private Label lblJugador;
    
    @FXML
    private ComboBox<Ataque> cmbAtaques;

    
    public void buscarPorNombre() {
        jugadorGeneral = mj.buscarJugador(txtNombre.getText());
        txtNombre.setText("");
        lblJugador.setText(jugadorGeneral.getNombre());
        
        
    }

    
    
    public void cargarDatos(Jugador jugador){
        this.jugadorGeneral = jugador;
        partida = new Partida(jugadorGeneral);
        lblJugador.setText(jugadorGeneral.getNombre());
        cmbAtaques.setItems(FXCollections.observableArrayList(jugadorGeneral.getAtaqueLista()));
        lblVida.setText(String.valueOf(jugadorGeneral.getVida()));
        lblMonocos.setText(String.valueOf(jugadorGeneral.getMonocos()));

        mostrarTextos();
        
    }

    public void Empezar() {
        new Thread(() -> {

            try {
                Random random = new Random();
                jugadorGeneral.ejecutarAtaqueEnemigo(random.nextInt(10)+5, jugadorGeneral.getSegundosVisibles(), verdad, txtArea, mj, rj);
                partida.setEnemigos(partida.ordenarEnemigos(partida.getEnemigos()));
                if(jugadorGeneral.getVida() <= 0){
                    jugadorGeneral.setVida(0);
                    partida.setJuegoAcabado(true);
                     Platform.runLater(() ->
        txtArea.appendText("Fue hit definitivamente. Te has muerto, eres la única persona de tu familia que ha perdido\nEres la vergüenza de tu linaje")
    );
                }
                mostrarTextos();
                
            } catch (InterruptedException e) {

                e.printStackTrace();
            }

        }).start();

        
    }

    private void mostrarTextos() {

        Platform.runLater(() -> {

        lblVida.setText(String.valueOf(jugadorGeneral.getVida()));
        lblMonocos.setText(String.valueOf(jugadorGeneral.getMonocos()));

        List<Jugador> enemigos = partida.getEnemigos();

        mostrarEnemigo(enemigos, 0, txtFirst, txtHfirst);
        mostrarEnemigo(enemigos, 1, txtSecond, txtHsecond);
        mostrarEnemigo(enemigos, 2, txtThird, txtHthird);
        mostrarEnemigo(enemigos, 3, txtFourth, txtHfourth);
        });
    }

    private void mostrarEnemigo(List<Jugador> enemigos, int index, Text nombre, Text vida) {

    if (index >= enemigos.size()) {
        nombre.setText("");
        vida.setText("");
        return;
    }

    Jugador j = enemigos.get(index);

    if (j.getVida() <= 0) {
        nombre.setText(j.getNombre());
        vida.setText("muelto");
    } else {
        nombre.setText(j.getNombre());
        vida.setText(String.valueOf(j.getVida()));
    }
}

}
