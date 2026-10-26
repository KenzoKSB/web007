package com.fatec.lojinha;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cliente")
@CrossOrigin(origins = "*")
public class ClienteController {
    @Autowired
    private ClienteRepository bd;

    @GetMapping
    public List<Cliente> listar() {
        return bd.findAll();
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Cliente> retorna(@PathVariable int codigo) {
        Optional<Cliente> cliente = bd.findById(codigo);
        return cliente.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cliente> gravar(@RequestBody Cliente obj) {
        obj.setCodigo(0);
        Cliente salvo = bd.save(obj);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Cliente> alterar(@PathVariable int codigo, @RequestBody Cliente obj) {
        if (!bd.existsById(codigo)) {
            return ResponseEntity.notFound().build();
        }
        obj.setCodigo(codigo);
        return ResponseEntity.ok(bd.save(obj));
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> apagar(@PathVariable int codigo) {
        if (!bd.existsById(codigo)) {
            return ResponseEntity.notFound().build();
        }
        bd.deleteById(codigo);
        return ResponseEntity.noContent().build();
    }
}
