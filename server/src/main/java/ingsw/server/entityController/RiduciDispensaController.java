package ingsw.server.entityController;

import ingsw.server.entityDAO.AvvisoDAO;
import ingsw.server.entityDAO.BachecaDAO;
import ingsw.server.entityDAO.DispensaDAO;
import ingsw.server.entityDAO.UtenteDAO;
import ingsw.server.entityDTO.dispensaDTO.RiduciQtaElemDispDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
@RequestMapping("controller/riduciDispensa")
public class RiduciDispensaController extends SuperController{
    public RiduciDispensaController(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    private String esisteElemDispensa(String nome){
        try{
            DispensaDAO dao = new DispensaDAO(connection);
            // recupero l'intero record dell' elemento della dispensa
            ResultSet rs = dao.recuperaElemDispensaByNome(nome);
            if(rs == null)
                return FALLIMENTO;
            // estrapolo solo la colonna Nome
            String nomeDaRS = ottieniColonnaDaResultSetSingolaRiga(rs, 1);
            if(nomeDaRS == null)    // se non ho trovato l'elemento cercato
                return FALLIMENTO;
            else{
                if(nomeDaRS.equals(nome)) return nome;
                else return FALLIMENTO;
            }
        }catch (SQLException e){
            return handleSQLException(e);
        }
    }
    //            -- USECASE: GENERA AVVISO IN BACHECA ---
    // funzione per creare l'avviso nella tabella Avvisi
    private String creaAvvisoDispensa(String elemento){
        try {
            AvvisoDAO dao = new AvvisoDAO(connection);
            // chiedo al Database di creare un avviso di soglia critica
            dao.inserisciAvviso("SOGLIA CRITICA", "QUANTITA' DI " + elemento + " AL DI SOTTO DELLA SOGLIA CRITICA");
        } catch (SQLException e){
            // la insert nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui gestisco l'effettiva eccezione (inevitabile) della insert
        return esitoQuery;
    }
    // funzione per recuperare l'avviso appena creato
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
    // funzione per gestire la creazione dell' avviso in bacheca e l'associazione all'elemento che lo ha generato
    private String generaAvvisoInBacheca(String nome){
        try {
            // Chiedo al Database di creare l'avviso in dispensa
            if(creaAvvisoDispensa(nome).equals(FALLIMENTO))
                return FALLIMENTO;          // se fallisce, la funzione termina con un fallimento in cascata

            AvvisoDAO dao = new AvvisoDAO(connection);
            // chiedo al Database l'avviso appena creato
            String idAvviso = recuperaIdUltimoAvvisoInserito();
            if(idAvviso.equals(FALLIMENTO))
                return FALLIMENTO;          // se fallisce, la funzione termina con un fallimento in cascata

            // associo l'avviso all'elemento della dispensa che lo ha generato
            dao.generaAvviso(nome, Integer.valueOf(idAvviso));
        } catch (SQLException e){
            // la insert nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile) della insert
        return esitoQuery;
    }
    // funzione per sapere la quantita a disposizione dell' elemento in dispensa
    private String quantitaAttualeElem(String nome){
        try{
            DispensaDAO dao = new DispensaDAO(connection);
            // recupero i dati dell'elemento in dispensa
            ResultSet rs = dao.recuperaElemDispensaByNome(nome);
            // recupero la colonna Quantita dell'elemento
            return ottieniColonnaDaResultSetSingolaRiga(rs, 5);
        } catch (SQLException e){
            esitoQuery = super.handleSQLException(e);
            return esitoQuery;
        }
    }
    // funzione per calcolare e ridurre la quantita dell' elemento in dispensa
    private String riduzioneQuantitaElem(String nome, Float qtaDaTogliere){
        try{
            DispensaDAO dao = new DispensaDAO(connection);
            // recupero la quantita attuale dell'elemento in dispensa
            String qtaAttuale = quantitaAttualeElem(nome);
            if(qtaAttuale.equals(FALLIMENTO))
                return FALLIMENTO;

            // effettuo la sottrazione per ottenere la quantita' finale che dovra' comparire in dispensa
            Float qtaFinale = Float.valueOf(qtaAttuale) - qtaDaTogliere;
            // effettuo l'update in dispensa
            dao.modificaQuantitaElemDispensa(nome, qtaFinale);
        } catch (SQLException e){
            // la modifica nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return esitoQuery;      // ritorna SUCCESSO se e' andato tutto bene
    }

    private String recuperaSogliaCritica(String nome){
        try{
            DispensaDAO dao = new DispensaDAO(connection);
            // recupero i dati dell'elemento in dispensa
            ResultSet rs = dao.recuperaElemDispensaByNome(nome);
            // recupero la colonna Soglia critica dell'elemento
            return ottieniColonnaDaResultSetSingolaRiga(rs, 6);
        } catch (SQLException e){
            esitoQuery = super.handleSQLException(e);
            return esitoQuery;
        }
    }
    private String confrontaDisponibilita(String disponibilita, String limite){
        Float dispo = Float.valueOf(disponibilita);
        Float limit = Float.valueOf(limite);
        if(dispo < limit)   return "SOTTO";
        else return "SOPRA";
    }
    private String sottoSogliaCritica(String nome){
        // recupero la quantita attuale dell'elemento
        String qtaAttuale = quantitaAttualeElem(nome);
        // recupero la soglia critica dell'elemento
        String sogliaCritica = recuperaSogliaCritica(nome);
        if(sogliaCritica.equals(FALLIMENTO) || qtaAttuale.equals(FALLIMENTO))
            return FALLIMENTO;
        // confronto la disponibilita con la soglia critica
        // ritorno SOTTO se si trova sotto la soglia critica, SOPRA se sta al pari o sopra la soglia critica
        return confrontaDisponibilita(qtaAttuale, sogliaCritica);
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

    private String inviaAresponsabili(Integer idAvviso){
        // invio a tutti gli ADMIN e SUPERVISOR l'avviso con codice idAvviso
        try{
            // recupero tutti gli utenti
            ResultSet elencoUtenti = recuperaTuttiUtenti();
            // per ogni dipendente assegno l'avviso in bacheca
            while(elencoUtenti.next()){
                // recupero l'informazione del campo 'Ruolo' dal record dell'utente
                String ruoloUtente = elencoUtenti.getString(5);
                // controllo che l'utente sia un ADMIN o un SUPERVISOR
                if(ruoloUtente.equals("Admin") || ruoloUtente.equals("Supervisor")){
                    // assegno l'avviso alla sua bacheca e salvo l'esito della query
                    String esito = inviaAUtente(idAvviso, elencoUtenti.getString(3));
                    // se qualcosa va storto esce
                    if(esito.equals(FALLIMENTO))
                        return FALLIMENTO;
                }
                // se l'utente non e' ne ADMIN ne SUPERVISOR non faccio nulla
            }
            return SUCCESSO;
        }catch (SQLException e){
            esitoQuery = handleSQLException(e);
            return esitoQuery;      // se qualcosa fallisce ritorno il FALLIMENTO
        }
    }

    @PatchMapping("riduciQta")
    public ResponseEntity<String> riduciQtaElemDispensa(@RequestBody RiduciQtaElemDispDTO input){
        // controllo che esiste l'elemento in dispensa
        if(esisteElemDispensa(input.getNome()).equals(FALLIMENTO))
            return ResponseEntity.badRequest().body("Elemento non trovato");

        // effettuo la riduzione di quantita in dispensa
        if(riduzioneQuantitaElem(input.getNome(), input.getTogli()).equals(FALLIMENTO))
            return ResponseEntity.badRequest().body("Errore nella riduzione della quantita");

        // controllo che la quantita sia scesa sotto la soglia critica
        String sottoSoglia = sottoSogliaCritica(input.getNome());

        // controllo che non abbia generato errore
        if(sottoSoglia.equals(FALLIMENTO))
            return ResponseEntity.badRequest().body("Errore nel calcolo della disponibilita rispetto alla soglia critica");

        // altrimenti, se e' andato tutto bene, termino con SUCCESSO se non e' scesa sotto la soglia critica
        if(sottoSoglia.equals("SOPRA"))
            return ResponseEntity.status(HttpStatus.OK).body("Quantita ridotta con successo");

        // mentre se e' scesa sotto la soglia critica
        if(sottoSoglia.equals("SOTTO")){
            // genero l'avviso da inserire sulla bacheca di Admin e Supervisor
            String avvisoGenerato = generaAvvisoInBacheca(input.getNome());

            // controllo che sia stato generato con successo
            if(avvisoGenerato.equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Errore nella creazione dell'avviso in bacheca");

            // recupero l'idAvviso dell'avviso appena generato
            String idAvviso = recuperaIdUltimoAvvisoInserito();

            // controllo di averlo effettivamente recuperato
            if(idAvviso.equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Errore nel recupero dell'id dell'avviso appena creato");

            // e quindi se l'ho recuperato con successo, lo converto nel valore Integer corrispondente
            Integer idAvvisoInt = Integer.valueOf(idAvviso);

            // assegno l'avviso ad admin e supervisor
            if(inviaAresponsabili(idAvvisoInt).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Errore nell' assegnazione dell'avviso ad admin e supervisor");

            // se va tutto bene restituisco il messaggio di SUCCESSO
            return ResponseEntity.status(HttpStatus.OK).body("Avviso in bacheca creato e quantita ridotta con successo");
        }
        // se dovesse andare qualcosa storto sopra, potrebbe ritornare SUCCESSO o FALLIMENTO in base a questa funzione messa di default
        return modificaEffettuata(esitoQuery);
    }

    /*@PatchMapping("aggiungiQta")
    public ResponseEntity<String> aggiungiQuantitaElem(@RequestBody AggiungiQtaElemDisp input){
        try{
            // controllo se esiste l'elemento in dispensa
            if(esisteElemDispensa(input.getNome()).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Elemento non trovato");

            // altrimenti, se esiste
            DispensaDAO dao = new DispensaDAO(connection);
            // recupero la quantita attuale dell'elemento in dispensa
            String qtaAttuale = quantitaAttualeElem(input.getNome());
            if(qtaAttuale.equals(FALLIMENTO))
                return ResponseEntity.badRequest().body(FALLIMENTO);

            // effettuo la sottrazione per ottenere la quantita' finale che dovra' comparire in dispensa
            Float qtaFinale = Float.valueOf(qtaAttuale) + input.getAggiungi();
            // effettuo l'update in dispensa
            dao.modificaQuantitaElemDispensa(input.getNome(), qtaFinale);
        } catch (SQLException e){
            // la modifica nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return modificaEffettuata(esitoQuery);
    }*/
}
