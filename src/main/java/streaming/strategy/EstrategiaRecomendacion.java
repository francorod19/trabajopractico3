package streaming.strategy;

import streaming.model.Cancion;
import java.util.List;

public interface EstrategiaRecomendacion {
    List<Cancion> recomendar(List<Cancion> catalogo, Cancion base);
}