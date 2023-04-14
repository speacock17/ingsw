package ingsw.server.entityDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DispensaDAO {
    private Connection connection;      // terra' traccia della connessione al Database
    // dichiaro le variabili in cui verranno inserite le query da eseguire:
    private PreparedStatement modificaElemDispensaByNomePS, modificaElemDispensaByDescrizionePS, modificaElemDispensaByCostoAcqPS;
    private PreparedStatement modificaElemDispensaByUdMPS, modificaElemDispensaByQuantitaPS, modificaElemDispensaBySogliaCriticaPS;
    private PreparedStatement recuperaDispensaPS, recuperaElemDispensaByNomePS, insertElemDispensaPS;
    private PreparedStatement eliminaElemDispensaByNomePS, eliminaElemDispensaPS;

    public DispensaDAO(Connection conn) throws SQLException {
        connection = conn;      // imposto la connessione di riferimento (sulla base della quale verranno gestite le query)

        // compilo le query:
        recuperaDispensaPS = connection.prepareStatement("SELECT * FROM DISPENSA");
        recuperaElemDispensaByNomePS = connection.prepareStatement("SELECT * FROM DISPENSA WHERE Nome = ?");
        insertElemDispensaPS = connection.prepareStatement("INSERT INTO DISPENSA VALUES(?,?,?,?,?,?)");
        modificaElemDispensaByNomePS = connection.prepareStatement("UPDATE DISPENSA SET Nome = ? WHERE Nome = ?");
        modificaElemDispensaByDescrizionePS = connection.prepareStatement("UPDATE DISPENSA SET Descrizione = ? WHERE Nome = ?");
        modificaElemDispensaByCostoAcqPS = connection.prepareStatement("UPDATE DISPENSA SET CostoAcquisto = ? WHERE Nome = ?");
        modificaElemDispensaByUdMPS = connection.prepareStatement("UPDATE DISPENSA SET UdM = ? WHERE Nome = ?");
        modificaElemDispensaByQuantitaPS = connection.prepareStatement("UPDATE DISPENSA SET Quantita = ? WHERE Nome = ?");
        modificaElemDispensaBySogliaCriticaPS = connection.prepareStatement("UPDATE DISPENSA SET SogliaCritica = ? WHERE Nome = ?");
        eliminaElemDispensaByNomePS = connection.prepareStatement("DELETE FROM DISPENSA WHERE Nome = ?");
        eliminaElemDispensaPS = connection.prepareStatement("DELETE FROM DISPENSA");
    }

    /*
        Create table Dispensa(
    Nome varchar(50) NOT NULL,
    Descrizione varchar(500) NOT NULL,
    CostoAcquisto float NOT NULL,
    UdM UnitaDiMisura NOT NULL,
    Quantita float NOT NULL,
    SogliaCritica float NOT NULL,
    */

    public void inserisciElemDispensa(String nome, String descrizione,
                                      Float costoAcq, String unitaDiMisura,
                                      Float quantita, Float sogliaCritica) throws SQLException{
        // assegno valori ai parametri delle query
        insertElemDispensaPS.setString(1, nome);
        insertElemDispensaPS.setString(2, descrizione);
        insertElemDispensaPS.setFloat(3, costoAcq);
        insertElemDispensaPS.setString(4, unitaDiMisura);
        insertElemDispensaPS.setFloat(5, quantita);
        insertElemDispensaPS.setFloat(6, sogliaCritica);
        insertElemDispensaPS.executeQuery();
    }

    public ResultSet recuperaDispensa() throws SQLException{
        // recupera TUTTI i valori degli elementi della dispensa, per OGNI elemento della dispensa
        return recuperaDispensaPS.executeQuery();
    }

    public ResultSet recuperaElemDispensaByNome(String nome) throws SQLException{
        // recupera TUTTI i valori di UN SOLO elemento della dispensa
        recuperaElemDispensaByNomePS.setString(1, nome);
        return recuperaElemDispensaByNomePS.executeQuery();
    }

    public void modificaNomeElemDispensa(String nome, String newNome) throws SQLException{
        // modifica Nome di un elemento della dispensa
        modificaElemDispensaByNomePS.setString(1, newNome);
        modificaElemDispensaByNomePS.setString(2, nome);
        modificaElemDispensaByNomePS.executeQuery();
    }

    public void modificaDescrizioneElemDispensa(String nome, String descrizione) throws SQLException{
        // modifica Descrizione di un elemento della dispensa
        modificaElemDispensaByDescrizionePS.setString(1, descrizione);
        modificaElemDispensaByDescrizionePS.setString(2, nome);
        modificaElemDispensaByDescrizionePS.executeQuery();
    }

    public void modificaCostoAcqElemDispensa(String nome, Float costoAcq) throws SQLException{
        // modifica Costo d'Acquisto di un elemento della dispensa
        modificaElemDispensaByCostoAcqPS.setFloat(1, costoAcq);
        modificaElemDispensaByCostoAcqPS.setString(2, nome);
        modificaElemDispensaByCostoAcqPS.executeQuery();
    }

    public void modificaUnitaDiMisuraElemDispensa(String nome, String unitaDiMisura) throws SQLException{
        // modifica Unita di Misura di un elemento della dispensa
        modificaElemDispensaByUdMPS.setString(1, unitaDiMisura);
        modificaElemDispensaByUdMPS.setString(2, nome);
        modificaElemDispensaByUdMPS.executeQuery();
    }

    public void modificaQuantitaElemDispensa(String nome, Float quantita) throws SQLException{
        // modifica Quantita di un elemento della dispensa
        modificaElemDispensaByQuantitaPS.setFloat(1, quantita);
        modificaElemDispensaByQuantitaPS.setString(2, nome);
        modificaElemDispensaByQuantitaPS.executeQuery();
    }

    public void modificaSogliaCriticaElemDispensa(String nome, Float sogliaCritica) throws SQLException{
        // modifica Soglia Critica di un elemento della dispensa
        modificaElemDispensaBySogliaCriticaPS.setFloat(1, sogliaCritica);
        modificaElemDispensaBySogliaCriticaPS.setString(2, nome);
        modificaElemDispensaBySogliaCriticaPS.executeQuery();
    }

    public void eliminaElemDispensaByNome(String nome) throws SQLException{
        // elimina un SOLO elemento della dispensa
        eliminaElemDispensaByNomePS.setString(1, nome);
        eliminaElemDispensaByNomePS.executeQuery();
    }

    public void eliminaDispensa() throws SQLException{
        // elimina TUTTI gli elementi della dispensa
        eliminaElemDispensaPS.executeQuery();
    }
}
