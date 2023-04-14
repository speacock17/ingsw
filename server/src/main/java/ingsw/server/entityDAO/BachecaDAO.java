package ingsw.server.entityDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BachecaDAO {

    private Connection connection;      // terra' traccia della connessione al Database
    // dichiaro le variabili in cui verranno inserite le query da eseguire:
    private PreparedStatement inserisciBachecaPS, recuperaTuttaBachecaPS, recuperaBachecaPS, recuperaVisibiliPS;
    private PreparedStatement recuperaNascostiPS, recuperaVistiPS, recuperaVistiNascostiPS;
    private PreparedStatement modificaVisualizzatoPS, modificaNascostoPS;
    private PreparedStatement eliminaTuttaBachecaPS, eliminaBachecaByUsernamePS;
    public BachecaDAO(Connection conn) throws SQLException{
        connection = conn;

        // compilo le query
        inserisciBachecaPS = connection.prepareStatement("INSERT INTO BACHECA VALUES (?, ?, ?, ?)");
        recuperaTuttaBachecaPS = connection.prepareStatement("SELECT * FROM BACHECA");
        recuperaBachecaPS = connection.prepareStatement("SELECT * FROM BACHECA WHERE Destinatario = ?");
        recuperaVisibiliPS = connection.prepareStatement("SELECT * FROM BACHECA WHERE Nascosto = false AND Destinatario = ?");
        recuperaNascostiPS = connection.prepareStatement("SELECT * FROM BACHECA WHERE Nascosto = true AND Destinatario = ?");
        recuperaVistiPS = connection.prepareStatement("SELECT * FROM BACHECA WHERE Visualizzato = true AND Nascosto = false AND Destinatario = ?");
        recuperaVistiNascostiPS = connection.prepareStatement("SELECT * FROM BACHECA WHERE Visualizzato = true AND Nascosto = true AND Destinatario = ?");
        modificaVisualizzatoPS = connection.prepareStatement("UPDATE BACHECA SET Visualizzato = ? WHERE IdAvviso = ? AND Destinatario = ?");
        modificaNascostoPS = connection.prepareStatement("UPDATE BACHECA SET Nascosto = ? WHERE IdAvviso = ? AND Destinatario = ?");
        eliminaTuttaBachecaPS = connection.prepareStatement("DELETE FROM BACHECA WHERE Destinatario = ?");
        eliminaBachecaByUsernamePS = connection.prepareStatement("DELETE FROM BACHECA WHERE Destinatario = ?");
    }
    /*
        Create table Bacheca(
    Visualizzato boolean NOT NULL,
    Nascosto boolean NOT NULL,
    IdAvviso integer NOT NULL,
    Destinatario varchar(50) NOT NULL,
     */

    public void inserisciBacheca(Boolean visualizzato, Boolean nascosto, Integer idAvviso, String destinatario) throws SQLException{
        // assegno i valori ai parametri della query
        inserisciBachecaPS.setBoolean(1, visualizzato);
        inserisciBachecaPS.setBoolean(2, nascosto);
        inserisciBachecaPS.setInt(3, idAvviso);
        inserisciBachecaPS.setString(4, destinatario);
        inserisciBachecaPS.executeQuery();
    }

    public ResultSet recuperaTuttaBacheca() throws SQLException{
        // recupero TUTTE le informazioni della tabella BACHECA
        return recuperaTuttaBachecaPS.executeQuery();
    }

    public ResultSet recuperaBacheca(String destinatario) throws SQLException{
        // recupera tutte le informazioni della bacheca di un utente
        recuperaBachecaPS.setString(1, destinatario);
        return recuperaBachecaPS.executeQuery();
    }

    public ResultSet recuperaVisibili(String destinatario) throws SQLException{
        // recupera tutti gli avvisi VISUALIZZABILI della bacheca di un utente
        recuperaVisibiliPS.setString(1, destinatario);
        return recuperaVisibiliPS.executeQuery();
    }

    public ResultSet recuperaNascosti(String destinatario) throws SQLException{
        // recupera tutti gli avvisi NASCOSTI della bacheca di un utente
        recuperaBachecaPS.setString(1, destinatario);
        return recuperaBachecaPS.executeQuery();
    }

    public ResultSet recuperaVisti(String destinatario) throws SQLException{
        // recupera tutti gli avvisi VISTI della bacheca di un utente
        recuperaBachecaPS.setString(1, destinatario);
        return recuperaBachecaPS.executeQuery();
    }

    public ResultSet recuperaVistiNascosti(String destinatario) throws SQLException{
        // recupera tutti gli avvisi VISTI e NASCOSTI della bacheca di un utente
        recuperaBachecaPS.setString(1, destinatario);
        return recuperaBachecaPS.executeQuery();
    }

    public void modificaVisualizzato(Boolean visualizzato, Integer idAvviso, String destinatario) throws SQLException{
        // modifica il campo Visualizzato di un certo AVVISO di un certo UTENTE
        modificaVisualizzatoPS.setBoolean(1, visualizzato);
        modificaVisualizzatoPS.setInt(2, idAvviso);
        modificaVisualizzatoPS.setString(3, destinatario);
        modificaVisualizzatoPS.executeQuery();
    }

    public void modificaNascosto(Boolean nascosto, Integer idAvviso, String destinatario) throws SQLException{
        // modifica il campo Nascosto di un certo AVVISO di un certo UTENTE
        modificaNascostoPS.setBoolean(1, nascosto);
        modificaNascostoPS.setInt(2, idAvviso);
        modificaNascostoPS.setString(3, destinatario);
        modificaNascostoPS.executeQuery();
    }

    public void eliminaTuttaBacheca() throws SQLException{
        // elimina tutti i record della tabella bacheca
        eliminaTuttaBachecaPS.executeQuery();
    }

    public void eliminaBachecaByUsername(String destinatario) throws SQLException{
        // elimina tutti i record della bacheca di un UTENTE
        eliminaBachecaByUsernamePS.setString(1, destinatario);
        eliminaBachecaByUsernamePS.executeQuery();
    }
}
