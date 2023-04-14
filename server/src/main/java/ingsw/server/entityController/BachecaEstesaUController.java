package ingsw.server.entityController;

import ingsw.server.entityDAO.BachecaEstesaUviewDAO;
import ingsw.server.entityDAO.UtenteDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
@RequestMapping("controller/bachecaEstesaU")
public class BachecaEstesaUController extends SuperController{
    public BachecaEstesaUController(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    private String esisteUtente(String username){
        try {
            UtenteDAO dao = new UtenteDAO(connection);
            // recupero l'intero record dell' utente
            ResultSet rs = dao.recuperaUtente(username);
            if(rs == null)
                return FALLIMENTO;
            // estrapolo solo la colonna Username
            String usernameDaRS = ottieniColonnaDaResultSetSingolaRiga(rs, 3);
            if(usernameDaRS == null)    // se non ho trovato l'username (quindi quell' utente non esiste)
                return FALLIMENTO;
            else {
                if(usernameDaRS.equals(username)) return username;    //ritorno l'username trovato
                else return FALLIMENTO;
            }
        } catch (SQLException e){
            return handleSQLException(e);
        }
    }

    @GetMapping("mostra/tutto")
    public ResponseEntity<String> mostraTuttaBacheca(){
        // restituisce una stringa elencata con tutti i valori dei record in bacheca
        try{
            BachecaEstesaUviewDAO dao = new BachecaEstesaUviewDAO(connection);

            ResultSet rs = dao.recuperaTuttaBacheca();
            return ResponseEntity.status(HttpStatus.OK).body(componiDaResultSet(rs));
        }catch (SQLException e){
            esitoQuery = super.handleSQLException(e);
            return ResponseEntity.badRequest().body(esitoQuery);
        }
    }

    @GetMapping("mostra/utente/tutti")
    public ResponseEntity<String> mostraBachecaUtente(@RequestParam(value = "username") String username){
        // restituisce una stringa elencata con tutti i valori dei record in bacheca DI UN UTENTE
        try{
            BachecaEstesaUviewDAO dao = new BachecaEstesaUviewDAO(connection);

            if(esisteUtente(username).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Utente non trovato");

            ResultSet rs = dao.recuperaBacheca(username);
            return ResponseEntity.status(HttpStatus.OK).body(componiDaResultSet(rs));
        }catch (SQLException e){
            esitoQuery = super.handleSQLException(e);
            return ResponseEntity.badRequest().body(esitoQuery);
        }
    }

    @GetMapping("mostra/utente/visibili")
    public ResponseEntity<String> mostraBachecaVisibiliUtente(@RequestParam(value = "username") String username){
        // restituisce una stringa elencata con i record VISIBILI in bacheca DI UN UTENTE
        try{
            BachecaEstesaUviewDAO dao = new BachecaEstesaUviewDAO(connection);

            if(esisteUtente(username).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Utente non trovato");

            ResultSet rs = dao.recuperaVisibili(username);
            return ResponseEntity.status(HttpStatus.OK).body(componiDaResultSet(rs));
        }catch (SQLException e){
            esitoQuery = super.handleSQLException(e);
            return ResponseEntity.badRequest().body(esitoQuery);
        }
    }

    @GetMapping("mostra/utente/nascosti")
    public ResponseEntity<String> mostraBachecaNascostiUtente(@RequestParam(value = "username") String username){
        // restituisce una stringa elencata i record NASCOSTI in bacheca DI UN UTENTE
        try{
            BachecaEstesaUviewDAO dao = new BachecaEstesaUviewDAO(connection);

            if(esisteUtente(username).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Utente non trovato");

            ResultSet rs = dao.recuperaNascosti(username);
            return ResponseEntity.status(HttpStatus.OK).body(componiDaResultSet(rs));
        }catch (SQLException e){
            esitoQuery = super.handleSQLException(e);
            return ResponseEntity.badRequest().body(esitoQuery);
        }
    }

    @GetMapping("mostra/utente/visti")
    public ResponseEntity<String> mostraBachecaVistiUtente(@RequestParam(value = "username") String username){
        // restituisce una stringa elencata i record VISTI in bacheca DI UN UTENTE
        try{
            BachecaEstesaUviewDAO dao = new BachecaEstesaUviewDAO(connection);

            if(esisteUtente(username).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Utente non trovato");

            ResultSet rs = dao.recuperaVisti(username);
            return ResponseEntity.status(HttpStatus.OK).body(componiDaResultSet(rs));
        }catch (SQLException e){
            esitoQuery = super.handleSQLException(e);
            return ResponseEntity.badRequest().body(esitoQuery);
        }
    }

    @GetMapping("mostra/utente/vistiNascosti")
    public ResponseEntity<String> mostraBachecaVistiNascostiUtente(@RequestParam(value = "username") String username){
        // restituisce una stringa elencata i record VISTI e NASCOSTI in bacheca DI UN UTENTE
        try{
            BachecaEstesaUviewDAO dao = new BachecaEstesaUviewDAO(connection);

            if(esisteUtente(username).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Utente non trovato");

            ResultSet rs = dao.recuperaVistiNascosti(username);
            return ResponseEntity.status(HttpStatus.OK).body(componiDaResultSet(rs));
        }catch (SQLException e){
            esitoQuery = super.handleSQLException(e);
            return ResponseEntity.badRequest().body(esitoQuery);
        }
    }
}
