package streaming.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import streaming.model.Album;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/albumes")
public class AlbumController {

    private List<Album> albumes = new ArrayList<>(); 

    
    @GetMapping
    public ResponseEntity<List<Album>> listarTodos() {
        return ResponseEntity.ok(albumes);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Album> buscarPorId(@PathVariable String id) {
        Optional<Album> album = albumes.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
                
        return album.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    
    @GetMapping("/buscar")
    public ResponseEntity<List<Album>> buscarPorTitulo(@RequestParam String titulo) {
        List<Album> resultados = albumes.stream()
                .filter(a -> a.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                .toList();

        if (resultados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultados);
    }
}