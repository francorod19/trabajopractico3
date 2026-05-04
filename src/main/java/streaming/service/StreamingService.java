package streaming.service;

import org.springframework.stereotype.Service;
import streaming.model.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StreamingService {
    
    // Usamos un ArrayList como base de datos en memoria para simplificar [cite: 152]
    private List<Cancion> catalogo = new ArrayList<>();

    public void agregarCancion(Cancion cancion) {
        catalogo.add(cancion);
    }

   // Constructor con tus propios datos
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

    // =========================================================================
    // 2.2 OPERACIONES CON STREAMS API (30%) 
    // =========================================================================

    // Filtrado compuesto [cite: 111]
    public List<Cancion> filtradoCompuesto(Genero genero, String nombreArtista, int anioInicio, int anioFin, double ratingMinimo) {
        return catalogo.stream()
                .filter(c -> c.getGenero() == genero)
                .filter(c -> c.getArtista().getNombre().equalsIgnoreCase(nombreArtista))
                .filter(c -> c.getFechaLanzamiento().getYear() >= anioInicio && c.getFechaLanzamiento().getYear() <= anioFin)
                .filter(c -> c.getRating() >= ratingMinimo)
                .toList();
    }

    // Top 10 más reproducidas [cite: 112]
    public List<Cancion> getTop10MasReproducidas() {
        return catalogo.stream()
                .sorted(Comparator.comparingInt(Cancion::getReproducciones).reversed())
                .limit(10)
                .toList();
    }

    // Estadísticas: Promedio de duración por género [cite: 114]
    public Map<Genero, Double> promedioDuracionPorGenero() {
        return catalogo.stream()
                .collect(Collectors.groupingBy(
                        Cancion::getGenero,
                        Collectors.averagingInt(Cancion::getDuracionSegundos)
                ));
    }

    // Estadísticas: Artista más popular [cite: 115]
    public Optional<Artista> artistaMasPopular() {
        return catalogo.stream()
                .collect(Collectors.groupingBy(Cancion::getArtista, Collectors.summingInt(Cancion::getReproducciones)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    // Estadísticas: Distribución por décadas 
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

    // Playlist automática (Aproximación Problema de la Mochila con recursión) 
    public List<Cancion> generarPlaylistExacta(int minutosExactos) {
        int segundosObjetivo = minutosExactos * 60;
        List<Cancion> playlist = new ArrayList<>();
        if (buscarCombinacionExacta(catalogo, segundosObjetivo, 0, playlist)) {
            return playlist;
        }
        return Collections.emptyList(); // Retorna vacío si no hay combinación exacta
    }

    // Método recursivo auxiliar para la mochila
    private boolean buscarCombinacionExacta(List<Cancion> opciones, int segundosRestantes, int indice, List<Cancion> seleccionadas) {
        if (segundosRestantes == 0) return true; // ¡Encontramos el tiempo exacto!
        if (segundosRestantes < 0 || indice >= opciones.size()) return false; // Nos pasamos o no hay más canciones

        Cancion actual = opciones.get(indice);
        
        // Camino 1: Incluir la canción actual
        seleccionadas.add(actual);
        if (buscarCombinacionExacta(opciones, segundosRestantes - actual.getDuracionSegundos(), indice + 1, seleccionadas)) {
            return true;
        }
        
        // Camino 2 (Backtracking): No incluir la canción y seguir buscando
        seleccionadas.remove(seleccionadas.size() - 1);
        return buscarCombinacionExacta(opciones, segundosRestantes, indice + 1, seleccionadas);
    }

    // =========================================================================
    // 2.3 ALGORITMOS DE BÚSQUEDA Y ORDENAMIENTO (20%) 
    // =========================================================================

    // Búsqueda binaria por título 
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

            if (comparacion == 0) return cancionMedio; // Encontrada
            if (comparacion < 0) inicio = medio + 1;   // Buscar en la mitad derecha
            else fin = medio - 1;                      // Buscar en la mitad izquierda
        }
        return null; // No encontrada
    }

    // Ordenamiento personalizado [cite: 124, 125]
    public List<Cancion> ordenamientoPersonalizado() {
        return catalogo.stream()
                .sorted(Comparator.comparing((Cancion c) -> c.getArtista().getNombre())
                        .thenComparing(Cancion::getFechaLanzamiento).reversed())
                .toList();
    }

    // Búsqueda lineal con predicados múltiples [cite: 126]
    public List<Cancion> busquedaLinealMultiple(Genero genero, int anioMayorA, double ratingMayorA) {
        List<Cancion> resultados = new ArrayList<>();
        // Búsqueda lineal clásica (for tradicional en lugar de Stream para variar el algoritmo)
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
