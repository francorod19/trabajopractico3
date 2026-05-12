package streaming.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import streaming.model.Cancion;
import streaming.service.StreamingService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/canciones")
public class CancionController {

    private final StreamingService service;

    public CancionController(StreamingService service) {
        this.service = service;
    }

    
    @GetMapping
    public ResponseEntity<List<Cancion>> listarTodas() {
        return ResponseEntity.ok(service.getCatalogo());
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Cancion> buscarPorId(@PathVariable String id) {
        Optional<Cancion> cancion = service.getCatalogo().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
        
       
        return cancion.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    
    @GetMapping("/buscar")
    public ResponseEntity<List<Cancion>> buscar(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String artista) {
        
        List<Cancion> resultados = service.getCatalogo().stream()
                .filter(c -> (titulo == null || c.getTitulo().equalsIgnoreCase(titulo)))
                .filter(c -> (artista == null || c.getArtista().getNombre().equalsIgnoreCase(artista)))
                .toList();

        if (resultados.isEmpty()) {
            return ResponseEntity.noContent().build(); 
        }
        return ResponseEntity.ok(resultados);
    }

    
    @PostMapping("/{id}/reproducir")
    public ResponseEntity<String> reproducir(@PathVariable String id) {
        Optional<Cancion> cancion = service.getCatalogo().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();

        if (cancion.isPresent()) {
            cancion.get().reproducir(); 
            return ResponseEntity.ok("Reproducción registrada. Total: " + cancion.get().getReproducciones());
        }
        return ResponseEntity.notFound().build();
    }
}