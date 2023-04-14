package ingsw.server.entityDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VariazioniDAO {
    private Connection connection;      // terra' traccia della connessione al Database
    // dichiaro le variabili in cui verranno inserite le query da eseguire:
    private PreparedStatement insertVariazionePS, recuperaVariazByIdOrdinePS, recuperaTutteVariazPS;
    private PreparedStatement modificaTipoVariazByIdOrdPS, eliminaVariazByIdOrdinePS, eliminaTutteVariazPS;

    public VariazioniDAO(Connection conn) throws SQLException {
        connection = conn;              // imposto la connessione di riferimento (sulla base della quale verranno gestite le query)

        // compilo le query:
        insertVariazionePS = connection.prepareStatement("INSERT INTO Variazioni VALUES (?, ?, ?)");
        recuperaVariazByIdOrdinePS = connection.prepareStatement("SELECT * FROM Variazioni WHERE IdOrdine = ?");
        recuperaTutteVariazPS = connection.prepareStatement("SELECT * FROM Variazioni");
        modificaTipoVariazByIdOrdPS = connection.prepareStatement("UPDATE Variazioni SET TipoVar = ? WHERE IdOrdine = ?");
        eliminaVariazByIdOrdinePS = connection.prepareStatement("DELETE FROM Variazioni WHERE IdOrdine = ?");
        eliminaTutteVariazPS = connection.prepareStatement("DELETE FROM Variazioni");
    }
    /*
        Create table Variazioni(
        IdOrdine integer NOT NULL,
        ProdDispensa varchar(50) NOT NULL,
        TipoVar TipoVariazione NOT NULL,
    */

    public void insertVariazione(Integer idOrdine,
                                 String prodDispensa,
                                 String tipoVariazione) throws SQLException{

        insertVariazionePS.setInt(1,idOrdine);
        insertVariazionePS.setString(2, prodDispensa);
        insertVariazionePS.setString(3, tipoVariazione);
        insertVariazionePS.executeQuery();
    }
    public ResultSet recuperaVariazByIdOrdine(Integer idOrdine) throws SQLException{
        recuperaVariazByIdOrdinePS.setInt(1, idOrdine);
        return recuperaVariazByIdOrdinePS.executeQuery();
    }
    public ResultSet recuperaTutteVariaz() throws SQLException{
        return recuperaTutteVariazPS.executeQuery();
    }
    public void modificaTipoVariazByIdOrd(Integer idOrdine) throws SQLException{
        modificaTipoVariazByIdOrdPS.setInt(1, idOrdine);
        modificaTipoVariazByIdOrdPS.executeQuery();
    }
    public void eliminaVariazByIdOrdine(Integer idOrdine) throws SQLException{
        eliminaVariazByIdOrdinePS.setInt(1, idOrdine);
        eliminaVariazByIdOrdinePS.executeQuery();
    }
    public void eliminaTutteVariaz() throws SQLException{
        eliminaTutteVariazPS.executeQuery();
    }
}
