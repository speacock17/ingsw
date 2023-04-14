package ingsw.server.entityDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AvvisoDAO {
    private Connection connection;      // terra' traccia della connessione al Database
    // dichiaro le variabili in cui verranno inserite le query da eseguire:
    private PreparedStatement inserisciAvvisoPS, eliminaAvvisoPS, eliminaTuttiAvvisiPS;
    private PreparedStatement scriveAvvisoPS, generaAvvisoPS, recuperaUltimoAvvisoPS;
    private PreparedStatement recuperaAvvisoByIdAvvisoPS, recuperaAvvisiPS;

    public AvvisoDAO(Connection conn) throws SQLException{
        connection = conn;

        // compilo le Query
        inserisciAvvisoPS = connection.prepareStatement("INSERT INTO Avviso VALUES (nextval('codAvviso'), ?, ?, DEFAULT)");
        scriveAvvisoPS = connection.prepareStatement("INSERT INTO MittenteU VALUES (?, ?)");
        generaAvvisoPS = connection.prepareStatement("INSERT INTO MittenteD VALUES (?, ?)");
        recuperaAvvisiPS = connection.prepareStatement("SELECT * FROM Avviso");
        recuperaAvvisoByIdAvvisoPS = connection.prepareStatement("SELECT * FROM Avviso WHERE IdAvviso = ?");
        recuperaUltimoAvvisoPS = connection.prepareStatement("SELECT * FROM Avviso WHERE IdAvviso = (SELECT max(IdAvviso) FROM Avviso)");
        eliminaAvvisoPS = connection.prepareStatement("DELETE FROM Avviso WHERE IdAvviso = ?");
        eliminaTuttiAvvisiPS = conn.prepareStatement("DELETE FROM Avviso");
    }
    /*
        Create table Avviso(
    IdAvviso integer NOT NULL,
    Oggetto varchar(50) NOT NULL,
    Testo varchar(500) NOT NULL,
    DataCreazione date NOT NULL default current_date,
    */
    /*
        Create table MittenteU(
    Mittente varchar(50) NOT NULL,
    IdAvviso integer NOT NULL,
     */
    /*
        Create table MittenteD(
    Mittente varchar(50) NOT NULL,
    IdAvviso integer NOT NULL,
    */

    public void inserisciAvviso(String Oggetto, String Testo) throws SQLException{
        // inserisce un avviso con idAvviso e DataCreazione automaticamente stabiliti dal Database
        inserisciAvvisoPS.setString(1, Oggetto);
        inserisciAvvisoPS.setString(2, Testo);
        inserisciAvvisoPS.executeQuery();
    }

    public void scriveAvviso(String username, Integer idAvviso) throws SQLException{
        // inserisce nella tabella Mittente U(tente) il record di associazione Utente-Avviso
        scriveAvvisoPS.setString(1, username);
        scriveAvvisoPS.setInt(2, idAvviso);
        scriveAvvisoPS.executeQuery();
    }

    public void generaAvviso(String elemento, Integer idAvviso) throws SQLException{
        // inserisce nella tabella Mittente D(ispensa) il record di associazione Dispensa-Avviso
        generaAvvisoPS.setString(1, elemento);
        generaAvvisoPS.setInt(2, idAvviso);
        generaAvvisoPS.executeQuery();
    }

    public ResultSet recuperaAvvisi() throws SQLException{
        // recupera ogni avviso
        return recuperaAvvisiPS.executeQuery();
    }

    public ResultSet recuperaAvviso(Integer idAvviso) throws SQLException{
        // recupera un avviso secondo un id
        recuperaAvvisoByIdAvvisoPS.setInt(1, idAvviso);
        return recuperaAvvisoByIdAvvisoPS.executeQuery();
    }

    public ResultSet recuperaUltimoAvviso() throws SQLException{
        // recupera l'ultimo avviso inserito nel database
        return recuperaUltimoAvvisoPS.executeQuery();
    }

    public void eliminaAvviso(Integer idAvviso) throws SQLException{
        // elimina l'avviso con un certo id
        eliminaAvvisoPS.setInt(1, idAvviso);
        eliminaAvvisoPS.executeQuery();
    }

    public void eliminaTuttiAvvisi() throws SQLException{
        // elimina tutti gli avvisi
        eliminaTuttiAvvisiPS.executeQuery();
    }
}
