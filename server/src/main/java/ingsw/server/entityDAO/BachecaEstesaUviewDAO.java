package ingsw.server.entityDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BachecaEstesaUviewDAO {
    private Connection connection;      // terra' traccia della connessione al Database
    // dichiaro le variabili in cui verranno inserite le query da eseguire:
    private PreparedStatement recuperaTuttaBachecaPS, recuperaBachecaPS, recuperaVisibiliPS;
    private PreparedStatement recuperaNascostiPS, recuperaVistiPS, recuperaVistiNascostiPS;
    public BachecaEstesaUviewDAO(Connection conn) throws SQLException {
        connection = conn;

        // compilo le query
        recuperaTuttaBachecaPS = connection.prepareStatement("SELECT * FROM BACHECAESTESAU");
        recuperaBachecaPS = connection.prepareStatement("SELECT * FROM BACHECAESTESAU WHERE Destinatario = ?");
        recuperaVisibiliPS = connection.prepareStatement("SELECT * FROM BACHECAESTESAU WHERE Nascosto = false AND Destinatario = ?");
        recuperaNascostiPS = connection.prepareStatement("SELECT * FROM BACHECAESTESAU WHERE Nascosto = true AND Destinatario = ?");
        recuperaVistiPS = connection.prepareStatement("SELECT * FROM BACHECAESTESAU WHERE Visualizzato = true AND Nascosto = false AND Destinatario = ?");
        recuperaVistiNascostiPS = connection.prepareStatement("SELECT * FROM BACHECAESTESAU WHERE Visualizzato = true AND Nascosto = true AND Destinatario = ?");
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
        recuperaNascostiPS.setString(1, destinatario);
        return recuperaNascostiPS.executeQuery();
    }

    public ResultSet recuperaVisti(String destinatario) throws SQLException{
        // recupera tutti gli avvisi VISTI della bacheca di un utente
        recuperaVistiPS.setString(1, destinatario);
        return recuperaVistiPS.executeQuery();
    }

    public ResultSet recuperaVistiNascosti(String destinatario) throws SQLException{
        // recupera tutti gli avvisi VISTI e NASCOSTI della bacheca di un utente
        recuperaVistiNascostiPS.setString(1, destinatario);
        return recuperaVistiNascostiPS.executeQuery();
    }
}
