package ingsw.server.entityController;

import ingsw.server.entityDAO.AvvisoDAO;
import ingsw.server.entityDAO.BachecaDAO;
import ingsw.server.entityDAO.UtenteDAO;
import ingsw.server.entityDTO.mittenteUtenteDTO.MailFormDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
@RequestMapping("controller/avvisiUtente")
public class AvvisiUtentiController extends SuperController{

    public AvvisiUtentiController(JdbcTemplate jdbcTemplate){ super(jdbcTemplate);}

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
            return handleSQLException(e);
        }
    }

    private String recuperaIdUltimoAvvisoInserito(){
        try {
            AvvisoDAO dao = new AvvisoDAO(connection);
            // richiedo al Database l'ultimo avviso
            ResultSet ultimoAvviso = dao.recuperaUltimoAvviso();
            // da cui mi estrapolo solo l'idAvviso
            return ottieniColonnaDaResultSetSingolaRiga(ultimoAvviso, 1);
        } catch (SQLException e){
            handleSQLException(e);
            return esitoQuery;
        }
    }

    private String inserisciAvviso(String oggetto, String testo){
        try{
            // accedo alla DAO per recuperare le query legate all'entita' Avviso
            AvvisoDAO dao = new AvvisoDAO(connection);

            //inserisco l'avviso nella tabella avvisi
            dao.inserisciAvviso(oggetto, testo);
        }catch (SQLException e){
            // la insert nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui gestisco l'eccezione (inevitabile) dell' inserimento
        return esitoQuery;
    }

    private String assegnaAutore(String username, Integer idAvviso){
        try {
            // accedo alla DAO per recuperare le query legate all'entita' Avviso
            AvvisoDAO dao = new AvvisoDAO(connection);
            // associo nella tabella mittenteU l'utente con l'avviso scritto
            dao.scriveAvviso(username, idAvviso);
        } catch (SQLException e){
            // la insert nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui gestisco l'eccezione (inevitabile) dell' inserimento
        return esitoQuery;
    }

    private ResultSet recuperaTuttiUtenti() throws SQLException{
        // potrebbe lanciare un eccezione che non spetta a questa funzione gestire
        UtenteDAO dao = new UtenteDAO(connection);
        // recupero tutti gli utenti e restituisco il resultset
        return dao.recuperaTuttiUtenti();
    }

    private String inviaAUtente(Integer idAvviso, String username){
        try{
            BachecaDAO dao = new BachecaDAO(connection);
            // assegno nella bacheca l'avviso all'utente
            dao.inserisciBacheca(false, false, idAvviso, username);
        } catch (SQLException e){
            // la insert nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui gestisco l'eccezione (inevitabile) dell' inserimento
        return esitoQuery;
    }

    private String inviaAtutti(Integer idAvviso){
        // invio a tutti i dipendenti l'avviso con codice idAvviso
        try{
            // recupero tutti gli utenti
            ResultSet elencoUtenti = recuperaTuttiUtenti();
            // per ogni dipendente assegno l'avviso in bacheca
            while(elencoUtenti.next()){
                // eseguo l'operazione per ogni utente e salvo l'esito della query
                String esito = inviaAUtente(idAvviso, elencoUtenti.getString(3));
                if(esito.equals(FALLIMENTO))
                    return FALLIMENTO;
            }
            return SUCCESSO;
        }catch (SQLException e){
            esitoQuery = handleSQLException(e);
            return esitoQuery;      // se qualcosa fallisce ritorno il FALLIMENTO
        }
    }

    @PostMapping("scriveAvviso")
    public ResponseEntity<String> scriveAvviso(@RequestBody MailFormDTO input){
        // effettua la registrazione nel database di un nuovo Avviso SCRITTO DA UN UTENTE

        // controllo se esiste l'utente che sta inserendo l' avviso
        if(esisteUsername(input.getUsername()).equals(FALLIMENTO))
            return ResponseEntity.badRequest().body("Utente non trovato");

        // se invece l'utente esiste
        // creo l'avviso
        if(inserisciAvviso(input.getOggetto(), input.getTesto()).equals(FALLIMENTO))
            return ResponseEntity.badRequest().body("Errore nella creazione dell'avviso");

        // recupero l'idAvviso dell'ultimo avviso inserito
        String idAvviso = recuperaIdUltimoAvvisoInserito();
        // controllo di averlo effettivamente recuperato
        if(idAvviso.equals(FALLIMENTO))
            return ResponseEntity.badRequest().body("Errore nel recupero dell'id dell'avviso appena creato");

        // altrimenti, se l'ho recuperato con successo, lo converto nel valore Integer corrispondente
        Integer idAvvisoInt = Integer.valueOf(idAvviso);

        // assegno l'avviso creato all'utente (AUTORE) che lo ha creato
        if(assegnaAutore(input.getUsername(), idAvvisoInt).equals(FALLIMENTO))
            return ResponseEntity.badRequest().body("Errore nell' assegnazione dell'autore all'avviso");

        // lo aggiungo alla bacheca di tutti i dipendenti
        if(inviaAtutti(idAvvisoInt).equals(FALLIMENTO))
            return ResponseEntity.badRequest().body("Errore nella compilazione della bacheca ai dipendenti");

        // qui effettivamente gestisco la doppia eccezione (inevitabile) sollevata dalle due insert
        return registrazioneEffettuata(esitoQuery);
    }

}
