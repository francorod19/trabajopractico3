package streaming.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Productora {
    private String id;
    private String nombre;
    private List<Artista> artistas;
    private List<Album> albumes;

    public Productora(String nombre) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.artistas = new ArrayList<>();
        this.albumes = new ArrayList<>();
    }

    // Getters y Setters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public List<Artista> getArtistas() { return artistas; }
    public List<Album> getAlbumes() { return albumes; }
}
