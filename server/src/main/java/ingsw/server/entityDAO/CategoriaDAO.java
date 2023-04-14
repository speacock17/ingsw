package ingsw.server.entityDAO;

import java.awt.geom.RectangularShape;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoriaDAO {
    private Connection connection;      // terra' traccia della connessione al Database
    // dichiaro le variabili in cui verranno inserite le query da eseguire:
    private PreparedStatement inserisciCategoriaPS, recuperaCategoriePS, recuperaCategByNomePS;
    private PreparedStatement recuperaUltimoPostoPS, modificaNomeCategPS, modificaPostoCategPS;
    private PreparedStatement eliminaCategByNomePS, eliminaCategoriePS;

    public CategoriaDAO(Connection conn) throws SQLException {
        connection = conn;      // imposto la connessione di riferimento (sulla base della quale verranno gestite le query)

        // compilo le query:
        inserisciCategoriaPS = connection.prepareStatement("INSERT INTO CATEGORIA VALUES (?, ?)");
        recuperaCategoriePS = connection.prepareStatement("SELECT * FROM CATEGORIA");
        recuperaCategByNomePS = connection.prepareStatement("SELECT * FROM CATEGORIA WHERE Nome = ?");
        recuperaUltimoPostoPS = connection.prepareStatement("SELECT * FROM CATEGORIA WHERE PostoMenu = (SELECT max(PostoMenu) FROM CATEGORIA)");
        modificaNomeCategPS = connection.prepareStatement("UPDATE CATEGORIA SET Nome = ? WHERE Nome = ?");
        modificaPostoCategPS = connection.prepareStatement("UPDATE CATEGORIA SET PostoMenu = ? WHERE Nome = ?");
        eliminaCategByNomePS = connection.prepareStatement("DELETE FROM CATEGORIA WHERE Nome = ?");
        eliminaCategoriePS = connection.prepareStatement("DELETE FROM CATEGORIA");
    }

    /*
        Create table Categoria(
    Nome varchar(50) NOT NULL,
    PostoMenu integer DEFAULT NULL,
     */
    public void inserisciCategoria(String nome, Integer postoMenu) throws SQLException{
        inserisciCategoriaPS.setString(1, nome);
        inserisciCategoriaPS.setInt(2, postoMenu);
        inserisciCategoriaPS.executeQuery();
    }

    public ResultSet recuperaCategByNome(String nome) throws SQLException{
        recuperaCategByNomePS.setString(1, nome);
        return recuperaCategByNomePS.executeQuery();
    }
    public ResultSet recuperaUltimoPosto() throws SQLException{
        return recuperaUltimoPostoPS.executeQuery();
    }

    public ResultSet recuperaTutteCateg() throws SQLException{
        return recuperaCategoriePS.executeQuery();
    }

    public void modificaNomeCateg(String nome, String newName) throws SQLException{
        modificaNomeCategPS.setString(1, newName);
        modificaNomeCategPS.setString(2, nome);
        modificaNomeCategPS.executeQuery();
    }

    public void modificaPostoCateg(String nome, Integer postoMenu) throws SQLException{
        modificaPostoCategPS.setInt(1, postoMenu);
        modificaPostoCategPS.setString(2, nome);
        modificaPostoCategPS.executeQuery();
    }

    public void eliminaCategByNome(String nome) throws SQLException{
        eliminaCategByNomePS.setString(1, nome);
        eliminaCategByNomePS.executeQuery();
    }

    public void eliminaCategorie() throws SQLException{
        eliminaCategoriePS.executeQuery();
    }
}
