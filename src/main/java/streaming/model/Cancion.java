package streaming.model;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Cancion {
    private String id; // UUID String
    private String titulo;
    private Artista artista; // Relación normalizada
    private Album album; // Relación normalizada
    private Genero genero;
    private int duracionSegundos;
    private AtomicInteger reproducciones; // Para concurrencia
    private double rating; // 0.0 a 5.0
    private LocalDate fechaLanzamiento;

    public Cancion(String titulo, Artista artista, Album album, Genero genero, int duracionSegundos, double rating, LocalDate fechaLanzamiento) {
        this.id = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.artista = artista;
        this.album = album;
        this.genero = genero;
        this.duracionSegundos = duracionSegundos;
        this.reproducciones = new AtomicInteger(0); // Arranca en 0
        this.rating = rating;
        this.fechaLanzamiento = fechaLanzamiento;
    }

    // Método para sumar reproducciones de forma segura en concurrencia
    public void reproducir() {
        this.reproducciones.incrementAndGet();
    }

    // Getters y Setters
    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public Artista getArtista() { return artista; }
    public void setArtista(Artista artista) { this.artista = artista; }
    public Album getAlbum() { return album; }
    public void setAlbum(Album album) { this.album = album; }
    public Genero getGenero() { return genero; }
    public void setGenero(Genero genero) { this.genero = genero; }
    public int getDuracionSegundos() { return duracionSegundos; }
    public void setDuracionSegundos(int duracionSegundos) { this.duracionSegundos = duracionSegundos; }
    public int getReproducciones() { return reproducciones.get(); }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public LocalDate getFechaLanzamiento() { return fechaLanzamiento; }
    public void setFechaLanzamiento(LocalDate fechaLanzamiento) { this.fechaLanzamiento = fechaLanzamiento; }
}