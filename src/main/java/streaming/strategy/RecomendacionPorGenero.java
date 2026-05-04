package streaming.strategy;

import streaming.model.Cancion;
import java.util.Comparator;
import java.util.List;

public class RecomendacionPorGenero implements EstrategiaRecomendacion {
    @Override
    public List<Cancion> recomendar(List<Cancion> catalogo, Cancion base) {
        return catalogo.stream()
                .filter(c -> c.getGenero() == base.getGenero()) // Mismo género
                .filter(c -> !c.getId().equals(base.getId())) // Que no sea la misma canción que estamos escuchando
                .sorted(Comparator.comparingDouble(Cancion::getRating).reversed()) // Mayor a menor rating
                .toList();
    }
}