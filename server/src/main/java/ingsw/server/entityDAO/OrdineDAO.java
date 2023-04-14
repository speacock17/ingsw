package ingsw.server.entityDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrdineDAO {
    private Connection connection;  // terra' traccia della connessione al Database
    // dichiaro le variabili in cui verranno inserite le query da eseguire:
    private PreparedStatement inserisciOrdinePS, recuperaOrdineByIdPS, recuperaTuttiOrdiniPS, recuperaOrdineByNumTavoloPS;
    private PreparedStatement modificaNumTavoloByIdPS, modificaQuantitaByIdPS, eliminaTuttiOrdiniPS, eliminaOrdineByIdPS;

    public OrdineDAO(Connection conn) throws SQLException {
        connection = conn;              // imposto la connessione di riferimento (sulla base della quale verranno gestite le query)

        // compilo le query:
        inserisciOrdinePS = connection.prepareStatement("INSERT INTO Ordine VALUES (nextval('codOrdine'),?,?,?)");
        recuperaOrdineByIdPS = connection.prepareStatement("SELECT * FROM Ordine WHERE IdOrdine = ?");
        recuperaTuttiOrdiniPS = connection.prepareStatement("SELECT * FROM Ordine");
        recuperaOrdineByNumTavoloPS = connection.prepareStatement("SELECT * FROM Ordine WHERE NumeroTavolo = ?");
        modificaNumTavoloByIdPS = connection.prepareStatement("UPDATE Ordine SET NumeroTavolo = ? WHERE IdOrdine = ?");
        modificaQuantitaByIdPS = connection.prepareStatement("UPDATE Ordine SET Quantita = ? WHERE IdOrdine = ?");
        eliminaTuttiOrdiniPS = connection.prepareStatement("DELETE FROM Ordine");
        eliminaOrdineByIdPS = connection.prepareStatement("DELETE  FROM Ordine WHERE IdOrdine = ?");
    }
    /*
        Create table Ordine(
        IdOrdine integer NOT NULL,
        NumeroTavolo integer NOT NULL,
        Quantita integer NOT NULL,
        Prodotto varchar(100) NOT NULL,
    */
    public void inserisciOrdinePS(Integer numeroTavolo, Integer quantita, String prodotto) throws SQLException{
        inserisciOrdinePS.setInt(1, numeroTavolo);
        inserisciOrdinePS.setInt(2, quantita);
        inserisciOrdinePS.setString(3, prodotto);
        inserisciOrdinePS.executeQuery();
    }
    public ResultSet recuperaOrdineByIdPS(Integer idOrdine) throws SQLException{
        recuperaOrdineByIdPS.setInt(1, idOrdine);
        return recuperaOrdineByIdPS.executeQuery();
    }
    public ResultSet recuperaTuttiOrdiniPS() throws SQLException{
        return recuperaTuttiOrdiniPS.executeQuery();
    }
    public ResultSet recuperaOrdineByNumTavoloPS(Integer numeroTavolo) throws SQLException{
        recuperaOrdineByNumTavoloPS.setInt(1, numeroTavolo);
        return recuperaOrdineByNumTavoloPS.executeQuery();
    }
    public void modificaNumTavoloByIdPS(Integer idOrdine, Integer numeroTavolo) throws SQLException{
        modificaNumTavoloByIdPS.setInt(1, numeroTavolo);
        modificaNumTavoloByIdPS.setInt(2, idOrdine);
        modificaNumTavoloByIdPS.executeQuery();
    }
    public void modificaQuantitaByIdPS(Integer idOrdine, Integer quantita) throws SQLException{
        modificaQuantitaByIdPS.setInt(1, quantita);
        modificaQuantitaByIdPS.setInt(2, idOrdine);
    }
    public void eliminaTuttiOrdiniPS() throws SQLException{
        eliminaTuttiOrdiniPS.executeQuery();
    }
    public void eliminaOrdineByIdPS(Integer idOrdine) throws SQLException{
        eliminaOrdineByIdPS.setInt(1, idOrdine);
        eliminaOrdineByIdPS.executeQuery();
    }
}
