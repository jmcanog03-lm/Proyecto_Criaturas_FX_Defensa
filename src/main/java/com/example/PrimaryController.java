package com.example;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.awt.Desktop;
import com.example.Modelo.Jugador;
import com.example.Modelo.ManagerJugador;
import com.example.Modelo.ReaderJugador;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author Emilio
 * @author Fabricio
 * @author JoseManuel
 */

public class PrimaryController implements Initializable {

    private Jugador jugadorGeneral;
    private ManagerJugador mj;
    private ReaderJugador rj;

    @FXML
    private TextField txtjugadorelegido;

    @FXML
    private Button btnEmpezar;

    @FXML
    private Button btnVentana;

    @FXML
    private ComboBox<Jugador> cbojugador;

    /**
     * iniciar todo el controlador
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
            try {
                Empezar();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


         btnVentana.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/andreherrera27200207-prog/Proyecto_Criaturas_FX"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

      
        List<Jugador> jugadores = new ArrayList<>();

        try {
            jugadores = rj.leer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        

        cbojugador.setItems(FXCollections.observableArrayList(jugadores));
        cbojugador.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Jugador item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });

        cbojugador.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Jugador item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });

        cbojugador.setOnAction(event -> {
            jugadorGeneral = cbojugador.getValue();

            if (jugadorGeneral != null) {
                txtjugadorelegido.setText(jugadorGeneral.getNombre());
            }
        });
    }

    /**
     * 
     * @throws IOException
     */
    public void Empezar() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/prueba.fxml"));
        Parent root = loader.load();

        PruebaController controllerSecond = loader.getController();

        if (jugadorGeneral != null) {
            controllerSecond.cargarDatos(jugadorGeneral);

            Scene escena = new Scene(root);
            Stage stage = (Stage) btnEmpezar.getScene().getWindow();
            stage.setScene(escena);
            stage.show();
        } else {
            txtjugadorelegido.setText("ERROR: Selecciona un jugador");
        }
    }
}
