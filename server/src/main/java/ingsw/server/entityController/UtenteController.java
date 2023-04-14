package ingsw.server.entityController;

import ingsw.server.entityDTO.utenteDTO.*;
import ingsw.server.entityDAO.UtenteDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;

@RestController         //serve per poter usare le notazioni e mappare i metodi
@RequestMapping("controller/utente")        // definisce il path per accedere tramite http ai metodi di questo controller
public class UtenteController extends SuperController {
    public UtenteController(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);        // inizializzo il controller
    }

    private String esisteUsername(String username){
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
            return super.handleSQLException(e);
        }
    }

    @PostMapping("registra")
    public ResponseEntity<String> insertUtente(@RequestParam(value = "ruolo") String ruolo,
                               @RequestBody RegistraFormDTO input){
        // effettua la registrazione nel database di un nuovo utente, dunque un utente con tipoDiRuolo ricevuto come parametro
        try {
            // accedo alla DAO per recuperare le query legate all'entita' utente
            UtenteDAO dao = new UtenteDAO(connection);
            // controllo se esiste gia un utente con l'username inviato dal client
            if(esisteUsername(input.getUsername()).equals(FALLIMENTO))
                // chiedo alla DAO di eseguire la query d'inserimento
                dao.inserisciUtente(
                        input.getNome(),
                        input.getCognome(),
                        input.getUsername(),
                        input.getPassword(),
                        ruolo);
            else    // esiste gia un utente con questo username
                return ResponseEntity.badRequest().body("Username gia in uso");
        }
        catch(SQLException e){
            // la insert nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return registrazioneEffettuata(esitoQuery);
    }

    @GetMapping("recupera/user")
    public ResponseEntity<String> recuperaUserNoPassword(@RequestParam(value = "username") String username){
        // restituisce una stringa elencata con i valori nome, cognome, username e ruolo di quell' utente
        try{
            UtenteDAO dao = new UtenteDAO(connection);
            // controllo esistenza dell' utente da cercare
            if(esisteUsername(username).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Utente non trovato");

            ResultSet rs = dao.recuperaUtenteNoPassword(username);
            return ResponseEntity.status(HttpStatus.OK).body(componiDaResultSet(rs));
        } catch (SQLException e){
            esitoQuery = super.handleSQLException(e);
            return ResponseEntity.badRequest().body(esitoQuery);
        }
    }

    @GetMapping("recupera/ruolo/{nomeRuolo}")
    public ResponseEntity<String> recuperaUserByRuolo(@PathVariable(value = "nomeRuolo") String ruolo){
        //restituisce un arraylist con i valori nome, cognome, username e ruolo di tutti gli utenti di un certo ruolo
        try {
            UtenteDAO dao = new UtenteDAO(connection);

            ResultSet rs = dao.recuperaUtenteByRuolo(ruolo);
            return ResponseEntity.status(HttpStatus.OK).body(componiDaResultSet(rs));
        }catch (SQLException e){
            esitoQuery = super.handleSQLException(e);
            return ResponseEntity.badRequest().body(esitoQuery);
        }
    }

    @GetMapping("recupera/tuttiUtenti")
    public ResponseEntity<String> recuperaTuttiUtenti(){
        // restituisce una stringa elencata con i valori nome, cognome, username, PASSWORD e ruolo di tutti gli utenti di un certo ruolo
        try {
            UtenteDAO dao = new UtenteDAO(connection);

            ResultSet rs = dao.recuperaTuttiUtenti();
            return ResponseEntity.status(HttpStatus.OK).body(componiDaResultSet(rs));
        }catch (SQLException e){
            esitoQuery = super.handleSQLException(e);
            return ResponseEntity.badRequest().body(esitoQuery);
        }
    }

    private String ottieniPassword(String username){
        try{
            UtenteDAO dao = new UtenteDAO(connection);

            ResultSet rs = dao.recuperaPasswordByUsername(username);
            if(rs == null)
                return FALLIMENTO;
            return ottieniColonnaDaResultSetSingolaRiga(rs, 1);
        } catch (SQLException e){
            esitoQuery = super.handleSQLException(e);
            return esitoQuery;
        }
    }

    private String verificaPrimoAccesso(String username){
        try {
            UtenteDAO dao = new UtenteDAO(connection);

            ResultSet rs = dao.recuperaTuttoUtenteByUsername(username);
            if(rs == null)
                return FALLIMENTO;
            return ottieniColonnaDaResultSetSingolaRiga(rs, 6);
        } catch (SQLException e){
            esitoQuery = super.handleSQLException(e);
            return esitoQuery;
        }
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginFormDTO input){
        // effettua il login cercando l'username nel database e confontando la password ricevuta con quella effettiva dell'utente
        try {
            UtenteDAO dao = new UtenteDAO(connection);
            // controllo esistenza dell' utente per effettuare il login
            if(esisteUsername(input.getUsername()).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Utente non trovato");

            //recupero la password
            String passwordUtente = ottieniPassword(input.getUsername());
            if(passwordUtente.equals(FALLIMENTO))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failure: Errore nel recupero della password");

            // se non ci sono stati problemi nel recupero della password
            // controllo che sia uguale a quella ricevuta dal client
            if(passwordUtente.equals(input.getPassword())){
                // controllo primo accesso
                String primoAccesso = verificaPrimoAccesso(input.getUsername());
                if(primoAccesso.equals(FALLIMENTO))
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failure: Errore nella verifica del primo accesso");

                if(primoAccesso.equals("t"))
                    return ResponseEntity.status(HttpStatus.OK).body("Successo: Login effettuato");
                // se e' false vuol dire che non ha mai fatto il primo accesso
                // metto il primo accesso a true
                // sollevera' sicuramente un eccezione
                dao.modificaPrimoAccessoUtente(input.getUsername(), true);
            }
            // se le password sono diverse
            else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failure: Password diverse");

        } catch (SQLException e) {
            // potrebbe sollevare un eccezione (INEVITABILE) se modifica il valore PrimoAccesso
            esitoQuery = super.handleSQLException(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Successo: Primo accesso effettuato");
    }

    @PatchMapping("update/nome")
    public ResponseEntity<String> updateNomeUtente(@RequestBody ModNomeUtenteDTO input){
        try {
            UtenteDAO dao = new UtenteDAO(connection);
            // controllo esistenza dell' utente da modificare
            if(esisteUsername(input.getUsername()).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Utente non trovato");

            dao.modificaNomeUtente(input.getUsername(), input.getNome());
        }catch(SQLException e){
            // la modifica nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return modificaEffettuata(esitoQuery);
    }

    @PatchMapping("update/cognome")
    public ResponseEntity<String> updateCognomeUtente(@RequestBody ModCognomeUtenteDTO input){
        try {
            UtenteDAO dao = new UtenteDAO(connection);
            // controllo esistenza dell' utente da modificare
            if(esisteUsername(input.getUsername()).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Utente non trovato");

            dao.modificaCognomeUtente(input.getUsername(), input.getCognome());
        }catch(SQLException e){
            // la modifica nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return modificaEffettuata(esitoQuery);
    }

    @PatchMapping("update/username")
    public ResponseEntity<String> updateUsernameUtente(@RequestBody ModUsernameUtenteDTO input){
        try {
            UtenteDAO dao = new UtenteDAO(connection);
            // controllo esistenza dell' utente da modificare
            if(esisteUsername(input.getUsername()).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Utente non trovato");

            // controllo se il nuovo username non sia gia in uso
            if(esisteUsername(input.getNewUsername()).equals(FALLIMENTO))
                dao.modificaUsernameUtente(input.getUsername(), input.getNewUsername());
            else    // esiste gia un utente con questo username
                return ResponseEntity.badRequest().body("Username gia in uso");

        }catch(SQLException e){
            // la modifica nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return modificaEffettuata(esitoQuery);
    }

    @PatchMapping("update/password")
    public ResponseEntity<String> updatePasswordUtente(@RequestBody ModPasswordUtenteDTO input){
        try {
            UtenteDAO dao = new UtenteDAO(connection);
            // controllo esistenza dell' utente da modificare
            if(esisteUsername(input.getUsername()).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Utente non trovato");

            dao.modificaPasswordUtente(input.getUsername(), input.getPassword());
        }catch(SQLException e){
            // la modifica nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return modificaEffettuata(esitoQuery);
    }

    @PatchMapping("update/ruolo")
    public ResponseEntity<String> updateRuoloUtente(@RequestBody ModRuoloUtenteDTO input){
        try {
            UtenteDAO dao = new UtenteDAO(connection);
            // controllo esistenza dell' utente da modificare
            if(esisteUsername(input.getUsername()).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Utente non trovato");

            dao.modificaRuoloUtente(input.getUsername(), input.getRuolo());
        }catch(SQLException e){
            // la modifica nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return modificaEffettuata(esitoQuery);
    }

    @DeleteMapping("delete/user")
    public ResponseEntity<String> deleteUtente(@RequestParam (value = "username") String username){
        try {
            UtenteDAO dao = new UtenteDAO(connection);
            // controllo esistenza dell' utente da eliminare
            if(esisteUsername(username).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Utente non trovato");

            dao.eliminaUtente(username);
        }catch(SQLException e){
            // la delete nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return eliminazioneEffettuata(esitoQuery);
    }

    @DeleteMapping("delete/ruolo/{nomeRuolo}")
    public ResponseEntity<String> deleteRuolo(@PathVariable (value = "nomeRuolo") String ruolo){
        try {
            UtenteDAO dao = new UtenteDAO(connection);

            dao.eliminaUtenteByRuolo(ruolo);
        }catch (SQLException e){
            // la delete nel database sollevera' sicuramente un eccezione quindi non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return eliminazioneEffettuata(esitoQuery);
    }

    @DeleteMapping("delete/tuttiUtenti")
    public ResponseEntity<String> deleteTuttiUtenti(){
        try {
            UtenteDAO dao = new UtenteDAO(connection);

            dao.eliminaTuttiUtenti();
        }catch (SQLException e){
            // la delete nel database sollevera' sicuramente un eccezione quindi non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return eliminazioneEffettuata(esitoQuery);
    }
}
