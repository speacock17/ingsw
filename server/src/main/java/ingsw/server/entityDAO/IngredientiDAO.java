package ingsw.server.entityDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IngredientiDAO {
    private Connection connection;          // terra' traccia della connessione al Database
    // dichiaro le variabili in cui verranno inserite le query da eseguire:

    private PreparedStatement inserisciIngredientiPS, modificaQuantitaPS, recuperaIngredientiByElemMenuPS;
    private PreparedStatement eliminaIngredientePS, eliminaIngredientiElemMenuPS, eliminaTuttiIngredientiPS;
    public IngredientiDAO(Connection conn) throws SQLException {
        connection = conn;            // imposto la connessione di riferimento (sulla base della quale verranno gestite le query)

        // compilo le query:
        inserisciIngredientiPS = connection.prepareStatement("INSERT INTO INGREDIENTI VALUES (?,?,?,?)");
        recuperaIngredientiByElemMenuPS = connection.prepareStatement("SELECT * FROM INGREDIENTI WHERE ElementoMenu = ?");
        modificaQuantitaPS = connection.prepareStatement("UPDATE INGREDIENTI SET Quantita = ? WHERE ElementoMenu = ? AND ElementoDispensa = ?");
        eliminaIngredientePS = connection.prepareStatement("DELETE FROM INGREDIENTI WHERE ElementoMenu = ? AND ElementoDispensa = ?");
        eliminaIngredientiElemMenuPS = connection.prepareStatement("DELETE FROM INGREDIENTI WHERE ElementoMenu = ?");
        eliminaTuttiIngredientiPS = connection.prepareStatement("DELETE FROM INGREDIENTI");
    }
    /*
        Create table Ingredienti(
        Quantita float NOT NULL,
        UdM UnitaDiMisura NOT NULL,     //DEVE ESSERE AUTOCOMPILATA
        ElementoMenu varchar(100) NOT NULL,
        ElementoDispensa varchar(50) NOT NULL,
    */
    public void inserisciIngredienti(Float quantita,
                                     String UdM,
                                     String elemMenu,
                                     String elemDispensa) throws SQLException{

        // UNITA DI MISURA DA AUTOCOMPILARE
        inserisciIngredientiPS.setFloat(1, quantita);
        inserisciIngredientiPS.setString(2, UdM);
        inserisciIngredientiPS.setString(3, elemMenu);
        inserisciIngredientiPS.setString(4, elemDispensa);
        inserisciIngredientiPS.executeQuery();
    }

    public ResultSet recuperaIngredientiByElemMenu(String elemMenu) throws SQLException{
        recuperaIngredientiByElemMenuPS.setString(1, elemMenu);
        return recuperaIngredientiByElemMenuPS.executeQuery();
    }

    public void modificaQuantita(String elemMenu, String elemDispensa, Float quantita) throws SQLException{
        modificaQuantitaPS.setFloat(1,quantita);
        modificaQuantitaPS.setString(2, elemMenu);
        modificaQuantitaPS.setString(3, elemDispensa);
        modificaQuantitaPS.executeQuery();
    }

    public void eliminaIngrediente(String elemMenu, String elemDispensa) throws SQLException{
        eliminaIngredientePS.setString(1, elemMenu);
        eliminaIngredientePS.setString(2, elemDispensa);
    }

    public void eliminaIngredientiElemMenu(String elemMenu) throws SQLException{
        eliminaIngredientiElemMenuPS.setString(1, elemMenu);
        eliminaIngredientiElemMenuPS.executeQuery();
    }

    public void eliminaTuttiIngredienti() throws SQLException{
        eliminaTuttiIngredientiPS.executeQuery();
    }
}
