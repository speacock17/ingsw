package ingsw.server.entityController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SuperController {
    protected Connection connection;
    protected final String SUCCESSO = "Successo", FALLIMENTO = "Fallimento", erroreResultSet = "Errore ResultSet";
    protected String stateSQL;
    protected JdbcTemplate jdbc;
    protected String esitoQuery = "Non ho ricevuto feedback dal Database";
    // se un controller stampa questa stringa qualcosa non va

    public SuperController(JdbcTemplate jdbcTemplate) {
        // costruttore che inizializza il controller
        try {
            jdbc = jdbcTemplate;
            connection = jdbc.getDataSource().getConnection();          // ottengo l'oggetto Connessione
        } catch (SQLException e) {
            System.out.println("Errore nel controller: "+ e.getMessage() +"\n"+ e.getSQLState());
        }
    }
    public String getSQLState() {
        return stateSQL;
    }
    protected String handleSQLException(SQLException e){
        // metodo che gestisce le eccezioni che ogni operazione al database lancia
        stateSQL = new String(e.getSQLState());         // assegno all'attributo lo stato di errore/successo
        if(stateSQL.equals("02000"))                      // se il codice di errore e' 02000 allora e' un successo del DB
            return SUCCESSO;
        else{
            System.out.println("Errore nel controller: "+ e.getMessage() +"\n"+ e.getSQLState());
            return FALLIMENTO;            // restituisco il messaggio di errore
        }
    }

    protected String componiDaResultSet(ResultSet rs) throws SQLException {
        // metodo che genera una stringa elencata delle informazioni riga per riga e colonna per colonna di una query
        String risultato = new String("");

        while(rs.next()){
            // per ogni colonna del resultset
            for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++){
                // compongo la stringa con ogni valore del risultato della query riga per riga e colonna per colonna
                risultato = risultato + rs.getMetaData().getColumnName(i) + " = " + rs.getString(i) + "\n";
            }
            risultato = risultato + "\n";
        }
        return risultato;
    }

    protected String ottieniColonnaDaResultSetSingolaRiga(ResultSet rs, int colonna) throws SQLException {
        // metodo che restituisce il valore di una colonna da un resultset che contiene una sola riga
        int numColonne = rs.getMetaData().getColumnCount();
        String risultato = null;

        if(colonna >0 && colonna <= numColonne){
            while(rs.next())    risultato =rs.getString(colonna);       //l'iterazione sara' singola poiché una sola colonna
            return risultato;
        }
        else {
            System.out.println("Indice di colonna non presente nel result set");
            return erroreResultSet;
        }
    }

    protected ResponseEntity<String> registrazioneEffettuata(String esitoQuery){
        if(esitoQuery.equals(SUCCESSO))
            return ResponseEntity.status(HttpStatus.OK).body("Successo: Registrazione effettuata ");
        else
            return ResponseEntity.badRequest().body(esitoQuery);
    }

    protected ResponseEntity<String> modificaEffettuata(String esitoQuery){
        if(esitoQuery.equals(SUCCESSO))
            return ResponseEntity.status(HttpStatus.OK).body("Successo: Modifica effettuata ");
        else
            return ResponseEntity.badRequest().body(esitoQuery);
    }

    protected ResponseEntity<String> eliminazioneEffettuata(String esitoQuery){
        if(esitoQuery.equals(SUCCESSO))
            return ResponseEntity.status(HttpStatus.OK).body("Successo: Eliminazione effettuata ");
        else
            return ResponseEntity.badRequest().body(esitoQuery);
    }
}
