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

    // 1. GET /api/canciones - listar todas [cite: 137]
    @GetMapping
    public ResponseEntity<List<Cancion>> listarTodas() {
        return ResponseEntity.ok(service.getCatalogo());
    }

    // 2. GET /api/canciones/{id} - buscar por ID [cite: 138]
    @GetMapping("/{id}")
    public ResponseEntity<Cancion> buscarPorId(@PathVariable String id) {
        Optional<Cancion> cancion = service.getCatalogo().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
        
        // Si la encuentra devuelve 200 OK, sino 404 Not Found
        return cancion.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3. GET /api/canciones/buscar?titulo=xxx&artista=yyy - búsqueda filtrada [cite: 139]
    @GetMapping("/buscar")
    public ResponseEntity<List<Cancion>> buscar(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String artista) {
        
        List<Cancion> resultados = service.getCatalogo().stream()
                .filter(c -> (titulo == null || c.getTitulo().equalsIgnoreCase(titulo)))
                .filter(c -> (artista == null || c.getArtista().getNombre().equalsIgnoreCase(artista)))
                .toList();

        if (resultados.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(resultados);
    }

    // 4. POST /api/canciones/{id}/reproducir - incrementar contador [cite: 140]
    @PostMapping("/{id}/reproducir")
    public ResponseEntity<String> reproducir(@PathVariable String id) {
        Optional<Cancion> cancion = service.getCatalogo().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();

        if (cancion.isPresent()) {
            cancion.get().reproducir(); // Usa el AtomicInteger de forma segura
            return ResponseEntity.ok("Reproducción registrada. Total: " + cancion.get().getReproducciones());
        }
        return ResponseEntity.notFound().build();
    }
}