package ingsw.server.entityDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MenuDAO {
    private Connection connection;      // terra' traccia della connessione al Database
    // dichiaro le variabili in cui verranno inserite le query da eseguire:
    private PreparedStatement inserisciElemMenuPS, recuperaMenuPS, recuperaMenuByNomePS;
    private PreparedStatement modificaNomeElemMenuPS, modificaDescrElemMenuPS, modificaCostoElemMenuPS;
    private PreparedStatement modificaAllergeniElemMenuPS, modificaPostoCategoriaElemMenuPS, modificaCategoriaElemMenuPS;
    private PreparedStatement recuperaMenuByCategoriaPS, eliminaElemMenuPS, eliminaTuttoMenuPS;

    public MenuDAO(Connection conn) throws SQLException {
        connection = conn;      // imposto la connessione di riferimento (sulla base della quale verranno gestite le query)

        // compilo le query:
        inserisciElemMenuPS = connection.prepareStatement("INSERT INTO MENU VALUES (?,?,?,?,?,?)");
        recuperaMenuPS = connection.prepareStatement("SELECT * FROM MENU");
        recuperaMenuByNomePS = connection.prepareStatement("SELECT * FROM MENU WHERE Nome = ?");
        recuperaMenuByCategoriaPS = connection.prepareStatement("SELECT * FROM MENU WHERE Categoria = ?");
        modificaNomeElemMenuPS = connection.prepareStatement("UPDATE MENU SET Nome = ? WHERE Nome = ?");
        modificaDescrElemMenuPS = connection.prepareStatement("UPDATE MENU SET Descrizione = ? WHERE Nome = ?");
        modificaCostoElemMenuPS = connection.prepareStatement("UPDATE MENU SET Costo = ? WHERE Nome = ?");
        modificaAllergeniElemMenuPS = connection.prepareStatement("UPDATE MENU SET Allergeni = ? WHERE Nome = ?");
        modificaPostoCategoriaElemMenuPS = connection.prepareStatement("UPDATE MENU SET PostoCategoria = ? WHERE Nome = ?");
        modificaCategoriaElemMenuPS = connection.prepareStatement("UPDATE MENU SET Categoria = ? WHERE Nome = ?");
        eliminaElemMenuPS = connection.prepareStatement("DELETE FROM MENU WHERE Nome = ?");
        eliminaTuttoMenuPS = connection.prepareStatement("DELETE FROM MENU");
    }
    /*
        Create table Menu(
    Nome varchar(100) NOT NULL,
    Descrizione varchar(500) NOT NULL,
    Costo float NOT NULL,
    Allergeni varchar(500) NOT NULL,
    Categoria varchar(50) DEFAULT NULL,
    PostoCategoria integer DEFAULT NULL,
     */
    public void inserisciElemMenu(String nome,
                                  String descrizione,
                                  Float costo,
                                  String allergeni,
                                  String categoria,
                                  Integer postoCategoria) throws SQLException{

        inserisciElemMenuPS.setString(1, nome);
        inserisciElemMenuPS.setString(2, descrizione);
        inserisciElemMenuPS.setFloat(3, costo);
        inserisciElemMenuPS.setString(4, allergeni);
        inserisciElemMenuPS.setString(5, categoria);
        inserisciElemMenuPS.setInt(6, postoCategoria);
        inserisciElemMenuPS.executeQuery();
    }
    public ResultSet recuperaMenuByNome(String nome) throws SQLException{
        recuperaMenuByNomePS.setString(1, nome);
        return recuperaMenuByNomePS.executeQuery();
    }

    public ResultSet recuperaMenuByCategoria(String categoria) throws SQLException{
        recuperaMenuByCategoriaPS.setString(1, categoria);
        return recuperaMenuByCategoriaPS.executeQuery();
    }
    public ResultSet recuperaMenu() throws SQLException{
        return recuperaMenuPS.executeQuery();
    }

    public void modificaNomeElemMenu(String nome, String newNome) throws SQLException{
       modificaNomeElemMenuPS.setString(1, newNome);
       modificaNomeElemMenuPS.setString(2, nome);
       modificaNomeElemMenuPS.executeQuery();
    }

    public void modificaDescrElemMenu(String nome, String descrizione) throws SQLException{
        modificaDescrElemMenuPS.setString(1, descrizione);
        modificaDescrElemMenuPS.setString(2, nome);
        modificaDescrElemMenuPS.executeQuery();
    }
    public void modificaCostoElemMenu(String nome, Float costo) throws SQLException{
        modificaCostoElemMenuPS.setFloat(1, costo);
        modificaCostoElemMenuPS.setString(2, nome);
        modificaCostoElemMenuPS.executeQuery();
    }
    public void modificaAllergeniElemMenu(String nome, String allergeni) throws SQLException{
        modificaAllergeniElemMenuPS.setString(1, nome);
        modificaAllergeniElemMenuPS.setString(2, allergeni);
        modificaAllergeniElemMenuPS.executeQuery();
    }
    public void modificaPostoCategoriaElemMenu(String nome, Integer postoCategoria) throws SQLException{
        modificaPostoCategoriaElemMenuPS.setInt(1, postoCategoria);
        modificaPostoCategoriaElemMenuPS.setString(2, nome);
        modificaPostoCategoriaElemMenuPS.executeQuery();
    }
    public void modificaCategoriaElemMenu(String nome, String categoria) throws SQLException{
        modificaCategoriaElemMenuPS.setString(1, categoria);
        modificaCategoriaElemMenuPS.setString(2, nome);
        modificaCategoriaElemMenuPS.executeQuery();
    }

    public void eliminaElemMenu(String nome) throws SQLException{
        eliminaElemMenuPS.setString(1, nome);
        eliminaElemMenuPS.executeQuery();
    }

    public void eliminaTuttoMenu() throws SQLException{
        eliminaTuttoMenuPS.executeQuery();
    }
}
