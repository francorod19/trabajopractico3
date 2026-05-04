package streaming.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import streaming.model.Artista;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/artistas")
public class ArtistaController {

    // Lista simulada para el ejemplo (en la realidad esto vendría de un ArtistaService)
    private List<Artista> artistas = new ArrayList<>(); 
    // Constructor para inyectar un artista de prueba al iniciar
    public ArtistaController() {
        streaming.model.Productora prod = new streaming.model.Productora("EMI");
        streaming.model.Artista queen = new streaming.model.Artista("Queen", prod);
        artistas.add(queen);
    }

    // GET /api/artistas - Listar todos [cite: 142]
    @GetMapping
    public ResponseEntity<List<Artista>> listarTodos() {
        return ResponseEntity.ok(artistas);
    }

    // GET /api/artistas/{id} - Buscar por ID [cite: 143]
    @GetMapping("/{id}")
    public ResponseEntity<Artista> buscarPorId(@PathVariable String id) {
        Optional<Artista> artista = artistas.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
                
        return artista.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET /api/artistas/buscar?nombre=xxx - Búsqueda simple [cite: 144]
    @GetMapping("/buscar")
    public ResponseEntity<List<Artista>> buscarPorNombre(@RequestParam String nombre) {
        List<Artista> resultados = artistas.stream()
                .filter(a -> a.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();

        if (resultados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultados);
    }
}