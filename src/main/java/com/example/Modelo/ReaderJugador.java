package com.example.Modelo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.Modelo.Efectos.Gommage;
import com.example.Modelo.Efectos.NuevaHabilidad;
import com.example.Modelo.Efectos.ParaAquellosQueVenganDespues;
import com.example.Modelo.Efectos.Potenciacion;
import com.example.Modelo.Efectos.Rayo;
import com.example.Modelo.Efectos.Ascua;
import com.example.Modelo.Efectos.Sobrecarga;
import com.example.Modelo.Efectos.TripleGolpe;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.Modelo.Efectos.Efecto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

 /**
     * @author Emilio
     * @author Fabricio
     * @author JoseManuel
     * @version 1.0
     * @since 1.0
     */
public class ReaderJugador implements InterfaceReaderJugador {

    /**
     * @param fichero
     * @param gson
     */
    private Path fichero;
    private Gson gson;

    public ReaderJugador() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }


    /**
     * 
     * @param dto
     * @return el jugador con su equipo
     */
    private Jugador convertir(JugadorDTO dto) {
        
        List<Ataque> ataques = new ArrayList<>();

        for (String nombreAtaque : dto.getAtaqueLista()) {
            ataques.add(crearAtaque(nombreAtaque));
        }

        return new Jugador(
            dto.getNombre(),
            dto.getVida(),
            dto.getMonocos(),
            dto.getDanoMultiplicador(),
            dto.getSegundosVisibles(),
            dto.getMonocosPorParry(),
            ataques
        );
    }

    /**
     * 
     * @param tipo
     * @return el ataque
     */
    private Ataque crearAtaque(String tipo) {
        Ataque ataque = new Ataque();
        switch (tipo) {
        case "Ascua": ataque.setEfecto(new Ascua());
                    ataque.setNombre(tipo);
                    ataque.setCoste(5);
                    break;

        case "Gommage": ataque.setEfecto(new Gommage());
                    ataque.setNombre(tipo);
                    ataque.setCoste(3);
                    break;

        case "ParaAquellosQueVenganDespues": ataque.setEfecto(new ParaAquellosQueVenganDespues());
                    ataque.setNombre(tipo);
                    ataque.setCoste(2);
                    break;

        case "Potenciacion": ataque.setEfecto(new Potenciacion());
                    ataque.setNombre(tipo);
                    ataque.setCoste(2);
                    break;

        case "Rayo": ataque.setEfecto(new Rayo());
                    ataque.setNombre(tipo);
                    ataque.setCoste(2);
                    break;

        case "Sobrecarga": ataque.setEfecto(new Sobrecarga());
                    ataque.setNombre(tipo);
                    ataque.setCoste(3);
                    break;

        case "NuevaHabilidad": ataque.setEfecto(new NuevaHabilidad());
                               ataque.setNombre(tipo);
                               ataque.setCoste(3);
                               break;
        case "TripleGolpe": ataque.setEfecto(new TripleGolpe());
                    ataque.setNombre(tipo);
                    ataque.setCoste(2);
                    break;
        default: throw new IllegalArgumentException("Ataque desconocido: " + tipo);
    }
    return ataque;
}
   /**
    * guardar jugador
    */
    @Override
    public void guardarJugador(Jugador jugador) throws Exception {

        List<Jugador> lista = leer();

        lista.add(jugador);

        String json = gson.toJson(lista);

        Files.writeString(fichero, json, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * 
     * @param lista_actualizada
     * @throws IOException
     */
    public void actualizarJSON(List<Jugador> lista_actualizada) throws IOException{
        String json = gson.toJson(lista_actualizada);
        Files.writeString(fichero, json, 
        java.nio.file.StandardOpenOption.CREATE, 
        java.nio.file.StandardOpenOption.TRUNCATE_EXISTING, 
        java.nio.file.StandardOpenOption.WRITE);
    }

    

   
    /**
     * @return jugador
     */
    public List<Jugador> leer() throws Exception {

    InputStream is = getClass().getResourceAsStream("/carpeta/jugadores.json");

    if (is == null) {
        throw new Exception("No se encontró el JSON en resources");
    }

    java.lang.reflect.Type tipoLista =
        new com.google.gson.reflect.TypeToken<ArrayList<JugadorDTO>>(){}.getType();

    List<JugadorDTO> dtos =
        gson.fromJson(new InputStreamReader(is), tipoLista);

    List<Jugador> jugadores = new ArrayList<>();

    if (dtos != null) {
        for (JugadorDTO dto : dtos) {
            jugadores.add(convertir(dto));
        }
    }

    return jugadores;
    }

    /**
     * 
     * @return mapa con los objetos jugadores
     */
    public Map<String, Jugador> cargarPersonajes() {
    ObjectMapper mapper = new ObjectMapper();

    try {
        InputStream is = getClass().getResourceAsStream("/carpeta/jugadores.json");

        if (is == null) {
            throw new Exception("¡No se encontró el archivo JSON!");
        }


        List<JugadorDTO> dtos =
                mapper.readValue(is, new TypeReference<List<JugadorDTO>>(){});


        List<Jugador> listaTemporal = new ArrayList<>();

        for (JugadorDTO dto : dtos) {
            listaTemporal.add(convertir(dto));
        }
        
        Map<String, Jugador> diccionario = listaTemporal.stream()
                .collect(Collectors.toMap(
                        Jugador::getNombre,
                        j -> j
                ));

        return diccionario;

    } catch (Exception e) {
        e.printStackTrace();
        return new HashMap<>();
    }
    
}
    

}
