package streaming.service;

import org.springframework.stereotype.Service;
import streaming.model.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StreamingService {
    
    
    private List<Cancion> catalogo = new ArrayList<>();

    public void agregarCancion(Cancion cancion) {
        catalogo.add(cancion);
    }

   
    public StreamingService() {
        Productora prod = new Productora("Sony Music");
        Artista soda = new Artista("Soda Stereo", prod);
        Album cancionAnimal = new Album("Canción Animal", soda);
        
        Cancion c1 = new Cancion("De Música Ligera", soda, cancionAnimal, Genero.ROCK, 212, 5.0, java.time.LocalDate.of(1990, 8, 7));
        Cancion c2 = new Cancion("Prófugos", soda, cancionAnimal, Genero.ROCK, 320, 4.9, java.time.LocalDate.of(1986, 11, 10));
        
        catalogo.add(c1);
        catalogo.add(c2);
    }
    public List<Cancion> getCatalogo() {
        return catalogo;
    }


    
    public List<Cancion> filtradoCompuesto(Genero genero, String nombreArtista, int anioInicio, int anioFin, double ratingMinimo) {
        return catalogo.stream()
                .filter(c -> c.getGenero() == genero)
                .filter(c -> c.getArtista().getNombre().equalsIgnoreCase(nombreArtista))
                .filter(c -> c.getFechaLanzamiento().getYear() >= anioInicio && c.getFechaLanzamiento().getYear() <= anioFin)
                .filter(c -> c.getRating() >= ratingMinimo)
                .toList();
    }

   
    public List<Cancion> getTop10MasReproducidas() {
        return catalogo.stream()
                .sorted(Comparator.comparingInt(Cancion::getReproducciones).reversed())
                .limit(10)
                .toList();
    }

    
    public Map<Genero, Double> promedioDuracionPorGenero() {
        return catalogo.stream()
                .collect(Collectors.groupingBy(
                        Cancion::getGenero,
                        Collectors.averagingInt(Cancion::getDuracionSegundos)
                ));
    }

    
    public Optional<Artista> artistaMasPopular() {
        return catalogo.stream()
                .collect(Collectors.groupingBy(Cancion::getArtista, Collectors.summingInt(Cancion::getReproducciones)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

     
    public Map<String, Long> distribucionPorDecadas() {
        return catalogo.stream()
                .collect(Collectors.groupingBy(
                        c -> {
                            int decada = (c.getFechaLanzamiento().getYear() / 10) * 10;
                            return decada + "s";
                        },
                        Collectors.counting()
                ));
    }

    
    public List<Cancion> generarPlaylistExacta(int minutosExactos) {
        int segundosObjetivo = minutosExactos * 60;
        List<Cancion> playlist = new ArrayList<>();
        if (buscarCombinacionExacta(catalogo, segundosObjetivo, 0, playlist)) {
            return playlist;
        }
        return Collections.emptyList(); 
    }

    
    private boolean buscarCombinacionExacta(List<Cancion> opciones, int segundosRestantes, int indice, List<Cancion> seleccionadas) {
        if (segundosRestantes == 0) return true; // ¡Encontramos el tiempo exacto!
        if (segundosRestantes < 0 || indice >= opciones.size()) return false; // Nos pasamos o no hay más canciones

        Cancion actual = opciones.get(indice);
        
        
        seleccionadas.add(actual);
        if (buscarCombinacionExacta(opciones, segundosRestantes - actual.getDuracionSegundos(), indice + 1, seleccionadas)) {
            return true;
        }
        
        
        seleccionadas.remove(seleccionadas.size() - 1);
        return buscarCombinacionExacta(opciones, segundosRestantes, indice + 1, seleccionadas);
    }

    
   
    public Cancion busquedaBinariaPorTitulo(String tituloBuscado) {
        // Primero preordenamos la lista alfabéticamente por título
        List<Cancion> listaOrdenada = catalogo.stream()
                .sorted(Comparator.comparing(Cancion::getTitulo, Comparator.naturalOrder()))
                .toList();

        int inicio = 0;
        int fin = listaOrdenada.size() - 1;

        while (inicio <= fin) {
            int medio = inicio + (fin - inicio) / 2;
            Cancion cancionMedio = listaOrdenada.get(medio);
            int comparacion = cancionMedio.getTitulo().compareToIgnoreCase(tituloBuscado);

            if (comparacion == 0) return cancionMedio; 
            if (comparacion < 0) inicio = medio + 1;   
            else fin = medio - 1;                      
        }
        return null; 
    }

    
    public List<Cancion> ordenamientoPersonalizado() {
        return catalogo.stream()
                .sorted(Comparator.comparing((Cancion c) -> c.getArtista().getNombre())
                        .thenComparing(Cancion::getFechaLanzamiento).reversed())
                .toList();
    }

    
    public List<Cancion> busquedaLinealMultiple(Genero genero, int anioMayorA, double ratingMayorA) {
        List<Cancion> resultados = new ArrayList<>();
        
        for (Cancion c : catalogo) {
            if (c.getGenero() == genero && 
                c.getFechaLanzamiento().getYear() > anioMayorA && 
                c.getRating() > ratingMayorA) {
                resultados.add(c);
            }
        }
        return resultados;
    }
}
