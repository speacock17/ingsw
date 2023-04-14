package ingsw.server.entityDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteDAO {
    private Connection connection;      // terra' traccia della connessione al Database
    // dichiaro le variabili in cui verranno inserite le query da eseguire:
    private PreparedStatement inserisciUtentePS, recuperaTuttiUtentiPS, recuperaUtenteByUsernameNoPasswordPS;
    private PreparedStatement eliminaTuttiUtentiPS, eliminaUtentePS;
    private PreparedStatement modificaUtenteByNomePS, modificaUtenteByCognomePS,modificaUtenteByRuoloPS, modificaPrimoAccessoByUsernamePS;
    private PreparedStatement modificaUtenteByPasswordPS, modificaUtenteByUsernamePS, recuperUtenteByRuoloPS, recuperaTuttoUtentePS;
    private PreparedStatement recuperaPasswordByUsernamePS, recuperaUtenteByUsernamePS, eliminaUtenteByRuoloPS;
    public UtenteDAO(Connection conn) throws SQLException {
        connection = conn;      // imposto la connessione di riferimento (sulla base della quale verranno gestite le query)

        // compilo le query:
        inserisciUtentePS = connection.prepareStatement("INSERT INTO UTENTE VALUES(?, ?, ?, ?, ?)");
        recuperaTuttiUtentiPS = connection.prepareStatement("SELECT * FROM UTENTE");
        recuperaUtenteByUsernamePS = connection.prepareStatement("SELECT * FROM UTENTE WHERE Username = ?");
        recuperaUtenteByUsernameNoPasswordPS = connection.prepareStatement("SELECT Nome, Cognome, Username, Ruolo FROM UTENTE WHERE Username = ?");
        recuperUtenteByRuoloPS = connection.prepareStatement("SELECT Nome, Cognome, Username, Ruolo FROM UTENTE WHERE Ruolo = ?::tipoUtente");
        recuperaPasswordByUsernamePS = connection.prepareStatement("SELECT Pass FROM UTENTE WHERE Username = ?");
        recuperaTuttoUtentePS = connection.prepareStatement("SELECT * FROM UTENTE WHERE Username = ?");
        modificaUtenteByNomePS = connection.prepareStatement("UPDATE UTENTE SET Nome = ? where Username = ?");
        modificaUtenteByCognomePS = connection.prepareStatement("UPDATE UTENTE SET Cognome = ? where Username = ?");
        modificaUtenteByUsernamePS = connection.prepareStatement("UPDATE UTENTE SET Username = ? where Username = ?");
        modificaUtenteByPasswordPS = connection.prepareStatement("UPDATE UTENTE SET Pass = ? where Username = ?");
        modificaUtenteByRuoloPS = connection.prepareStatement("UPDATE UTENTE SET Ruolo = ? where Username = ?");
        modificaPrimoAccessoByUsernamePS = connection.prepareStatement("UPDATE UTENTE SET PrimoAccesso = ? WHERE Username = ?");
        eliminaUtentePS = connection.prepareStatement("DELETE FROM UTENTE WHERE Username = ?");
        eliminaUtenteByRuoloPS = connection.prepareStatement("DELETE FROM UTENTE WHERE Ruolo = ?::tipoUtente");
        eliminaTuttiUtentiPS = connection.prepareStatement("DELETE FROM UTENTE");
    }
    /*
        Create table Utente(
    Nome varchar(50) NOT NULL,
    Cognome varchar(50) NOT NULL,
    Username varchar(50) NOT NULL,
    Pass varchar(50) NOT NULL,
    Ruolo tipoUtente NOT NULL,
    PrimoAccesso bool DEFAULT false,
    */

    public void inserisciUtente(String nome, String cognome, String username, String password, String ruolo) throws SQLException {
        // assegno valori ai parametri delle query
        inserisciUtentePS.setString(1, nome);
        inserisciUtentePS.setString(2, cognome);
        inserisciUtentePS.setString(3, username);
        inserisciUtentePS.setString(4, password);
        inserisciUtentePS.setString(5, ruolo);
        inserisciUtentePS.executeQuery();       //eseguo la query (istruzione individuata dallo statement)
    }

    public ResultSet recuperaUtenteNoPassword(String username) throws SQLException{
        // recupera tutte le informazioni di un preciso utente TRANNE LA PASSWORD
        recuperaUtenteByUsernameNoPasswordPS.setString(1, username);
        return recuperaUtenteByUsernameNoPasswordPS.executeQuery();
    }

    public ResultSet recuperaPasswordByUsername(String username) throws  SQLException{
        // recupera SOLO LA PASSWORD di un preciso utente
        recuperaPasswordByUsernamePS.setString(1, username);
        return recuperaPasswordByUsernamePS.executeQuery();
    }

    public ResultSet recuperaUtente(String username) throws SQLException{
        // recupera tutte le informazioni di un preciso utente
        recuperaUtenteByUsernamePS.setString(1, username);
        return recuperaUtenteByUsernamePS.executeQuery();
    }

    public ResultSet recuperaUtenteByRuolo(String ruolo) throws SQLException{
        // recupera gli username in base al ruolo
        recuperUtenteByRuoloPS.setString(1, ruolo);
        return recuperUtenteByRuoloPS.executeQuery();
    }

    public ResultSet recuperaTuttiUtenti() throws SQLException{
        // recupera tutti gli utenti
        return recuperaTuttiUtentiPS.executeQuery();
    }

    public ResultSet recuperaTuttoUtenteByUsername(String username) throws SQLException{
        // recupera tutte le informazioni di un utente
        recuperaTuttoUtentePS.setString(1, username);
        return recuperaTuttoUtentePS.executeQuery();
    }

    public void modificaNomeUtente(String username, String nome) throws SQLException{
        // modifica il nome utente
        modificaUtenteByNomePS.setString(1, nome);
        modificaUtenteByNomePS.setString(2, username);
        modificaUtenteByNomePS.executeQuery();
    }

    public void modificaCognomeUtente(String username, String cognome) throws SQLException{
        // modifica il cognome utente
        modificaUtenteByCognomePS.setString(1, cognome);
        modificaUtenteByCognomePS.setString(2, username);
        modificaUtenteByCognomePS.executeQuery();
    }

    public void modificaUsernameUtente(String username, String newUsername) throws SQLException{
        // modifica lo Username utente
        modificaUtenteByUsernamePS.setString(1, newUsername);
        modificaUtenteByUsernamePS.setString(2, username);
        modificaUtenteByUsernamePS.executeQuery();
    }

    public void modificaPasswordUtente(String username, String password) throws SQLException{
        // modifica la Password utente
        modificaUtenteByPasswordPS.setString(1, password);
        modificaUtenteByPasswordPS.setString(2, username);
        modificaUtenteByPasswordPS.executeQuery();
    }

    public void modificaRuoloUtente(String username, String ruolo) throws SQLException{
        // modifica il Ruolo utente
        modificaUtenteByRuoloPS.setString(1, ruolo);
        modificaUtenteByRuoloPS.setString(2, username);
        modificaUtenteByRuoloPS.executeQuery();
    }

    public void modificaPrimoAccessoUtente(String username, Boolean accesso) throws SQLException{
        // modifica il Ruolo utente
        modificaPrimoAccessoByUsernamePS.setBoolean(1, accesso);
        modificaPrimoAccessoByUsernamePS.setString(2, username);
        modificaPrimoAccessoByUsernamePS.executeQuery();
    }

    public void eliminaUtente(String username) throws SQLException{
        // elimina utente
        eliminaUtentePS.setString(1, username);
        eliminaUtentePS.executeQuery();
    }

    public void eliminaUtenteByRuolo(String ruolo) throws SQLException{
        // elimina per ruolo
        eliminaUtenteByRuoloPS.setString(1, ruolo);
        eliminaUtenteByRuoloPS.executeQuery();
    }

    public void eliminaTuttiUtenti() throws SQLException{
        // elimina tutti gli utenti
        eliminaTuttiUtentiPS.executeQuery();
    }
}
