package streaming.strategy;

import streaming.model.Cancion;
import java.util.Comparator;
import java.util.List;

public class RecomendacionPorPopularidad implements EstrategiaRecomendacion {
    @Override
    public List<Cancion> recomendar(List<Cancion> catalogo, Cancion base) {
        return catalogo.stream()
                .filter(c -> !c.getId().equals(base.getId())) 
                .sorted(Comparator.comparingInt(Cancion::getReproducciones).reversed()) // Mayor a menor reproducciones
                .limit(5) // Solo las 5 mejores
                .toList();
    }
}