package streaming.strategy;

import streaming.model.Cancion;
import java.time.LocalDate;
import java.util.List;

public class RecomendacionDescubrimiento implements EstrategiaRecomendacion {
    @Override
    public List<Cancion> recomendar(List<Cancion> catalogo, Cancion base) {
        LocalDate limiteFecha = LocalDate.now().minusYears(2); // Fecha de hace exactamente 2 años

        return catalogo.stream()
                .filter(c -> c.getReproducciones() < 1000) // Menos de 1000 reproducciones
                .filter(c -> c.getFechaLanzamiento().isAfter(limiteFecha)) // Reciente (lanzada después del límite)
                .filter(c -> c.getGenero() != base.getGenero()) // Género distinto al habitual
                .toList();
    }
}