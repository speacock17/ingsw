package ingsw.server.entityController;

import ingsw.server.entityDTO.menuDTO.ElemMenuFormDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController         //serve per poter usare le notazioni e mappare i metodi
@RequestMapping("controller/menu")      // definisce il path per accedere tramite http ai metodi di questo controller
public class MenuController extends SuperController{
    public MenuController(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @PostMapping("registra")
    public ResponseEntity<String> insertElemMenu(@RequestBody ElemMenuFormDTO input){
        return registrazioneEffettuata(esitoQuery);
    }
}
