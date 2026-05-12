package streaming.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import streaming.model.Productora;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productoras")
public class ProductoraController {

    private List<Productora> productoras = new ArrayList<>(); 

    
    @GetMapping
    public ResponseEntity<List<Productora>> listarTodas() {
        return ResponseEntity.ok(productoras);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Productora> buscarPorId(@PathVariable String id) {
        Optional<Productora> productora = productoras.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
                
        return productora.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    
    @GetMapping("/buscar")
    public ResponseEntity<List<Productora>> buscarPorNombre(@RequestParam String nombre) {
        List<Productora> resultados = productoras.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();

        if (resultados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultados);
    }
}