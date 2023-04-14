package ingsw.server.entityDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LinguaStranieraDAO {
    private Connection connection;          // terra' traccia della connessione al Database
    // dichiaro le variabili in cui verranno inserite le query da eseguire:
    private PreparedStatement inserisciLinguaStranieraPS, recuperaLinguaStranieraPS, recuperaElemLinguaStrPS;
    private PreparedStatement modificaLinguaElemLingStrPS, modificaDescrElemMenuPS, modificaAllergeniElemMenuPS, modificaNomeLingStrPS;
    private PreparedStatement eliminaTuttoMenuPS, eliminaElemMenuPS;

    public LinguaStranieraDAO(Connection conn) throws SQLException {
        connection = conn;          // imposto la connessione di riferimento (sulla base della quale verranno gestite le query)

        // compilo le query:
        inserisciLinguaStranieraPS = connection.prepareStatement("INSERT INTO LinguaStraniera VALUES (?,?,?,?,?)");
        recuperaLinguaStranieraPS = connection.prepareStatement("SELECT * FROM LinguaStraniera");
        recuperaElemLinguaStrPS = connection.prepareStatement("SELECT * FROM LinguaStraniera WHERE WHERE ElementoMenu = ?");
        modificaLinguaElemLingStrPS = connection.prepareStatement("UPDATE LinguaStraniera SET Lingua = ? WHERE ElementoMenu = ?");
        modificaNomeLingStrPS = connection.prepareStatement("UPDATE LinguaStraniera SET NomeProdotto = ? WHERE ElementoMenu = ?");
        modificaDescrElemMenuPS = connection.prepareStatement("UPDATE LinguaStraniera SET DescrizioneProdotto = ? WHERE ElementoMenu = ?");
        modificaAllergeniElemMenuPS = connection.prepareStatement("UPDATE LinguaStraniera SET Allergeni = ? WHERE ElementoMenu = ?");
        eliminaElemMenuPS = connection.prepareStatement("DELETE FROM LinguaStraniera WHERE ElementoMenu = ?");
        eliminaTuttoMenuPS = connection.prepareStatement("DELETE FROM LinguaStraniera");
    }
    /*
    Create table LinguaStraniera(
        Lingua varchar(50) NOT NULL,
        NomeProdotto varchar(100) NOT NULL,
        DescrizioneProdotto varchar(500) NOT NULL,
        Allergeni varchar(500) NOT NULL,
        ElementoMenu varchar(100) NOT NULL,
     */
    public void inserisciLinguaStraniera(String lingua,
                                         String nomeProdotto,
                                         String descrizioneProd,
                                         String allergeni,
                                         String elementoMenu) throws SQLException{
        inserisciLinguaStranieraPS.setString(1, lingua);
        inserisciLinguaStranieraPS.setString(2, nomeProdotto);
        inserisciLinguaStranieraPS.setString(3, descrizioneProd);
        inserisciLinguaStranieraPS.setString(4, allergeni);
        inserisciLinguaStranieraPS.setString(5, elementoMenu);
        inserisciLinguaStranieraPS.executeQuery();
    }
    public ResultSet recuperaLinguaStraniera() throws SQLException{
        return recuperaLinguaStranieraPS.executeQuery();
    }
    public ResultSet recuperaElemLinguaStr(String elemMenu) throws SQLException{
        recuperaElemLinguaStrPS.setString(1, elemMenu);
        return recuperaElemLinguaStrPS.executeQuery();
    }
    public void modificaLinguaElemLingStr(String elemMenu, String lingua) throws SQLException{
        modificaLinguaElemLingStrPS.setString(1, lingua);
        modificaLinguaElemLingStrPS.setString(2, elemMenu);
        modificaLinguaElemLingStrPS.executeQuery();
    }
    public void modificaNomeLingStr(String elemMenu, String nomeProdotto) throws SQLException{
        modificaNomeLingStrPS.setString(1, nomeProdotto);
        modificaNomeLingStrPS.setString(2, elemMenu);
        modificaNomeLingStrPS.executeQuery();
    }
    public void modificaDescrElemMenu(String elemMenu, String descrizioneProd) throws SQLException{
        modificaDescrElemMenuPS.setString(1, descrizioneProd);
        modificaDescrElemMenuPS.setString(2, elemMenu);
        modificaDescrElemMenuPS.executeQuery();
    }
    public void modificaAllergeniElemMenu(String elemMenu, String allergeni) throws SQLException{
        modificaAllergeniElemMenuPS.setString(1, allergeni);
        modificaAllergeniElemMenuPS.setString(2, elemMenu);
        modificaAllergeniElemMenuPS.executeQuery();
    }
    public void eliminaElemMenu(String elemMenu) throws SQLException{
        eliminaElemMenuPS.setString(1, elemMenu);
        eliminaElemMenuPS.executeQuery();
    }
    public void eliminaTuttoMenu() throws SQLException{
        eliminaTuttoMenuPS.executeQuery();
    }


    /*
    Create table LinguaStraniera(
        Lingua varchar(50) NOT NULL,
        NomeProdotto varchar(100) NOT NULL,
        DescrizioneProdotto varchar(500) NOT NULL,
        Allergeni varchar(500) NOT NULL,
        ElementoMenu varchar(100) NOT NULL,
     */
}
