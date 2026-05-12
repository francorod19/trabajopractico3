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

    
    private List<Artista> artistas = new ArrayList<>(); 
    
    public ArtistaController() {
        streaming.model.Productora prod = new streaming.model.Productora("EMI");
        streaming.model.Artista queen = new streaming.model.Artista("Queen", prod);
        artistas.add(queen);
    }

   
    @GetMapping
    public ResponseEntity<List<Artista>> listarTodos() {
        return ResponseEntity.ok(artistas);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Artista> buscarPorId(@PathVariable String id) {
        Optional<Artista> artista = artistas.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
                
        return artista.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    
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