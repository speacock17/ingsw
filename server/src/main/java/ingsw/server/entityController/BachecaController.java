package ingsw.server.entityController;

import ingsw.server.entityDAO.BachecaDAO;
import ingsw.server.entityDTO.bachecaDTO.ModNascostoDTO;
import ingsw.server.entityDTO.bachecaDTO.ModVisualizzatoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("controller/bacheca")
public class BachecaController extends SuperController{
    public BachecaController(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @PatchMapping("modifica/nascosto")
    public ResponseEntity<String> updateStatoNascostoBacheca(@RequestBody ModNascostoDTO input){
        try{
            BachecaDAO dao = new BachecaDAO(connection);
            // salto il controllo sull'id avviso
            // salto il controllo sull'username

            dao.modificaNascosto(input.getNascosto(), input.getIdAvviso(), input.getUsername());
        }catch (SQLException e){
            // la modifica nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return modificaEffettuata(esitoQuery);
    }

    @PatchMapping("modifica/visualizzato")
    public ResponseEntity<String> updateStatoVisualizzatoBacheca(@RequestBody ModVisualizzatoDTO input){
        try{
            BachecaDAO dao = new BachecaDAO(connection);
            // salto il controllo sull'id avviso
            // salto il controllo sull'username

            dao.modificaVisualizzato(input.getVisualizzato(), input.getIdAvviso(), input.getUsername());
        }catch (SQLException e){
            // la modifica nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return modificaEffettuata(esitoQuery);
    }
}
