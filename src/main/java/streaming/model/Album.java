package streaming.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Album {
    private String id;
    private String titulo;
    private Artista artista;
    private List<Cancion> canciones;

    public Album(String titulo, Artista artista) {
        this.id = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.artista = artista;
        this.canciones = new ArrayList<>();
    }

    // Getters y Setters
    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public Artista getArtista() { return artista; }
    public void setArtista(Artista artista) { this.artista = artista; }
    public List<Cancion> getCanciones() { return canciones; }
}