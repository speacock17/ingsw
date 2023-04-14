package ingsw.server.entityController;

import ingsw.server.entityDAO.AvvisoDAO;
import ingsw.server.entityDTO.avvisoDTO.BodyTextAvvisoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
@RequestMapping("controller/avviso")
public class AvvisoController extends SuperController{
    public AvvisoController(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    private String esisteAvviso(String idAvviso){
        try{
            AvvisoDAO dao = new AvvisoDAO(connection);
            // recupero l'intero record dell' avviso
            ResultSet rs = dao.recuperaAvviso(Integer.valueOf(idAvviso));
            if(rs == null)
                return FALLIMENTO;
            // estrapolo solo la colonna IdAvviso
            String idAvvisoDaRS = ottieniColonnaDaResultSetSingolaRiga(rs, 1);
            if(idAvvisoDaRS == null)
                return FALLIMENTO;
            else{
                if(idAvvisoDaRS.equals(idAvviso)) return idAvviso;
                else return FALLIMENTO;
            }
        }catch (SQLException e){
            return super.handleSQLException(e);
        }
    }

    @PostMapping("registra")
    public ResponseEntity<String> insertAvviso(@RequestBody BodyTextAvvisoDTO input){
        // effettua la registrazione nel database di un nuovo Avviso
        try{
            // accedo alla DAO per recuperare le query legate all'entita' Avviso
            AvvisoDAO dao = new AvvisoDAO(connection);
            // chiedo alla DAO di eseguire la query d'inserimento
            dao.inserisciAvviso(input.getOggetto(), input.getTesto());
        } catch (SQLException e){
            // la insert nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return registrazioneEffettuata(esitoQuery);
    }

    @GetMapping("recupera/avviso")
    public ResponseEntity<String> recuperaAvviso(@RequestParam (value = "idAvviso") String idAvviso){
        // restituisce una stringa elencata con i valori IdAvviso, Oggetto, Testo e Data creazione
        try {
            AvvisoDAO dao = new AvvisoDAO(connection);
            // controllo esistenza dell' avviso da cercare
            if(esisteAvviso(idAvviso).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Avviso non trovato");

            ResultSet rs = dao.recuperaAvviso(Integer.valueOf(idAvviso));
            return ResponseEntity.status(HttpStatus.OK).body(componiDaResultSet(rs));

        } catch (SQLException e){
            esitoQuery = super.handleSQLException(e);
            return ResponseEntity.badRequest().body(esitoQuery);
        }
    }

    @GetMapping("recupera/tuttiAvvisi")
    public ResponseEntity<String> recuperaTuttiAvvisi(){
        // restituisce una stringa elencata con i valori di TUTTI gli avvisi
        try{
            AvvisoDAO dao = new AvvisoDAO(connection);
            ResultSet rs = dao.recuperaAvvisi();
            return ResponseEntity.status(HttpStatus.OK).body(componiDaResultSet(rs));

        }catch (SQLException e){
            esitoQuery = super.handleSQLException(e);
            return ResponseEntity.badRequest().body(esitoQuery);
        }
    }

    @DeleteMapping("delete/avviso")
    public ResponseEntity<String> deleteAvviso(@RequestParam (value = "idAvviso") String idAvviso){
        try {
            AvvisoDAO dao = new AvvisoDAO(connection);
            // controllo esistenza dell' avviso da eliminare
            if(esisteAvviso(idAvviso).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Avviso non trovato");

            dao.eliminaAvviso(Integer.valueOf(idAvviso));
        }catch (SQLException e){
            // la delete nel database sollevera' sicuramente un eccezione quindi non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return eliminazioneEffettuata(esitoQuery);
    }

    @DeleteMapping("delete/tuttiAvvisi")
    public ResponseEntity<String> deleteAvviso(){
        try {
            AvvisoDAO dao = new AvvisoDAO(connection);

            dao.eliminaTuttiAvvisi();
        }catch (SQLException e){
            // la delete nel database sollevera' sicuramente un eccezione quindi non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return eliminazioneEffettuata(esitoQuery);
    }
}
