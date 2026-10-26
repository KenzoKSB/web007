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
@RequestMapping("/api/pedido")
@CrossOrigin(origins = "*")
public class PedidoController {
    @Autowired
    private PedidoRepository bd;

    @GetMapping
    public List<Pedido> listar() {
        return bd.findAll();
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Pedido> retorna(@PathVariable int codigo) {
        Optional<Pedido> pedido = bd.findById(codigo);
        return pedido.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pedido> gravar(@RequestBody Pedido obj) {
        obj.setCodigoPedido(0);
        if (obj.getItens() != null) {
            obj.getItens().forEach(item -> item.setPedido(obj));
        }
        Pedido salvo = bd.save(obj);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Pedido> alterar(@PathVariable int codigo, @RequestBody Pedido obj) {
        if (!bd.existsById(codigo)) {
            return ResponseEntity.notFound().build();
        }
        obj.setCodigoPedido(codigo);
        if (obj.getItens() != null) {
            obj.getItens().forEach(item -> item.setPedido(obj));
        }
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
