package streaming.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Artista {
    private String id;
    private String nombre;
    private Productora productora;
    private List<Album> albumes;

    public Artista(String nombre, Productora productora) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.productora = productora;
        this.albumes = new ArrayList<>();
    }

    // Getters y Setters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Productora getProductora() { return productora; }
    public void setProductora(Productora productora) { this.productora = productora; }
    public List<Album> getAlbumes() { return albumes; }
}