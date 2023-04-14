package ingsw.server.entityController;

import ingsw.server.entityDAO.DispensaDAO;
import ingsw.server.entityDTO.dispensaDTO.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
@RequestMapping("controller/dispensa")
public class DispensaController extends SuperController{
    public DispensaController(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    private String esisteElemDispensa(String nome){
        try{
            DispensaDAO dao = new DispensaDAO(connection);
            // recupero l'intero record dell' elemento della dispensa
            ResultSet rs = dao.recuperaElemDispensaByNome(nome);
            if(rs == null)
                return FALLIMENTO;
            // estrapolo solo la colonna Nome
            String nomeDaRS = ottieniColonnaDaResultSetSingolaRiga(rs, 1);
            if(nomeDaRS == null)    // se non ho trovato l'elemento cercato
                return FALLIMENTO;
            else{
                if(nomeDaRS.equals(nome)) return nome;
                else return FALLIMENTO;
            }
        }catch (SQLException e){
            return handleSQLException(e);
        }
    }

    @PostMapping("registra")
    public ResponseEntity<String> insertElemDispensa(@RequestBody RegistraDispensaDTO input){
        // effettua la registrazione nel database di un nuovo elemento della dispensa
        try{
            // accedo alla DAO per recuperare le query legate all'entita' dispensa
            DispensaDAO dao = new DispensaDAO(connection);
            // controllo se esiste gia un utente con il nome inviato dal client
            if(esisteElemDispensa(input.getNome()).equals(FALLIMENTO))
                // chiedo alla DAO di eseguire la query di inserimento
                dao.inserisciElemDispensa(
                        input.getNome(),
                        input.getDescrizione(),
                        input.getCostoAcq(),
                        input.getUnitaDiMisura(),
                        input.getQuantita(),
                        input.getSogliaCritica()
                );
            else    // esiste gia un elemento in dispensa con questo nome
                return ResponseEntity.badRequest().body("Nome gia in uso");
        }
        catch(SQLException e){
            // la insert nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return registrazioneEffettuata(esitoQuery);
    }

    @GetMapping("recupera/elemDispensa")
    public ResponseEntity<String> recuperaElemDispensa(@RequestBody NomeElemDispDTO input){
        // restituisce una stringa elencata con i valori:
        // nome, descrizione, costo acquisto, unita di misura, quantita, soglia critica
        try{
            DispensaDAO dao = new DispensaDAO(connection);
            // controllo esistenza dell' elemento da cercare
            if(esisteElemDispensa(input.getNome()).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Elemento non trovato");

            ResultSet rs = dao.recuperaElemDispensaByNome(input.getNome());
            return  ResponseEntity.status(HttpStatus.OK).body(componiDaResultSet(rs));

        }catch (SQLException e){
            esitoQuery = super.handleSQLException(e);
            return ResponseEntity.badRequest().body(esitoQuery);
        }
    }

    @GetMapping("recupera/dispensa")
    public ResponseEntity<String> recuperaDispensa(){
        // restituisce una stringa elencata con tutti i dettagli di TUTTI gli elementi della dispensa
        try{
            DispensaDAO dao = new DispensaDAO(connection);

            ResultSet rs = dao.recuperaDispensa();
            return ResponseEntity.status(HttpStatus.OK).body(componiDaResultSet(rs));
        }catch (SQLException e){
            esitoQuery = super.handleSQLException(e);
            return ResponseEntity.badRequest().body(esitoQuery);
        }
    }

    @PatchMapping("update/nome")
    public ResponseEntity<String> updateNomeElemDispensa(@RequestBody ModNomeElemDispDTO input){
        try{
            DispensaDAO dao = new DispensaDAO(connection);
            // controllo esistenza dell' elemento da modificare
            if(esisteElemDispensa(input.getNome()).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Elemento non trovato");

            // controllo se il nuovo nome non sia gia in uso
            if(esisteElemDispensa(input.getNewNome()).equals(FALLIMENTO))
                dao.modificaNomeElemDispensa(input.getNome(), input.getNewNome());
            else
                return ResponseEntity.badRequest().body("Nome elemento dispensa gia in uso");
        } catch(SQLException e){
            // la modifica nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return modificaEffettuata(esitoQuery);
    }

    @PatchMapping("update/descrizione")
    public ResponseEntity<String> updateDescrizioneElemDispensa(@RequestBody ModDescrElemDispDTO input){
        try{
            DispensaDAO dao = new DispensaDAO(connection);
            // controllo esistenza dell' elemento da modificare
            if(esisteElemDispensa(input.getNome()).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Elemento non trovato");

            dao.modificaDescrizioneElemDispensa(input.getNome(), input.getDescrizione());
        } catch(SQLException e){
            // la modifica nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return modificaEffettuata(esitoQuery);
    }

    @PatchMapping("update/costoAcq")
    public ResponseEntity<String> updateCostoAcqElemDispensa(@RequestBody ModCostoAcqElemDispDTO input){
        try{
            DispensaDAO dao = new DispensaDAO(connection);
            // controllo esistenza dell' elemento da modificare
            if(esisteElemDispensa(input.getNome()).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Elemento non trovato");

            dao.modificaCostoAcqElemDispensa(input.getNome(), input.getCostoAcq());
        } catch(SQLException e){
            // la modifica nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return modificaEffettuata(esitoQuery);
    }

    @PatchMapping("update/unitaDiMisura")
    public ResponseEntity<String> updateUdMElemDispensa(@RequestBody ModUdMElemDispDTO input){
        try{
            DispensaDAO dao = new DispensaDAO(connection);
            // controllo esistenza dell' elemento da modificare
            if(esisteElemDispensa(input.getNome()).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Elemento non trovato");

            dao.modificaUnitaDiMisuraElemDispensa(input.getNome(), input.getUnitaDiMisura());
        } catch(SQLException e){
            // la modifica nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return modificaEffettuata(esitoQuery);
    }

    @PatchMapping("update/quantita")
    public ResponseEntity<String> updateQuantitaElemDispensa(@RequestBody ModQtaElemDispDTO input){
        try{
            DispensaDAO dao = new DispensaDAO(connection);
            // controllo esistenza dell' elemento da modificare
            if(esisteElemDispensa(input.getNome()).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Elemento non trovato");

            dao.modificaQuantitaElemDispensa(input.getNome(), input.getQuantita());
        } catch(SQLException e){
            // la modifica nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return modificaEffettuata(esitoQuery);
    }

    @PatchMapping("update/sogliaCritica")
    public ResponseEntity<String> updateSogliaCriticaElemDispensa(@RequestBody ModSogliaCritElemDispDTO input){
        try{
            DispensaDAO dao = new DispensaDAO(connection);
            // controllo esistenza dell' elemento da modificare
            if(esisteElemDispensa(input.getNome()).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Elemento non trovato");

            dao.modificaSogliaCriticaElemDispensa(input.getNome(), input.getSogliaCritica());
        } catch(SQLException e){
            // la modifica nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return modificaEffettuata(esitoQuery);
    }

    @DeleteMapping("delete/elemDispensa")
    public ResponseEntity<String> deleteElemDispensa(@RequestBody NomeElemDispDTO input){
        try{
            DispensaDAO dao = new DispensaDAO(connection);
            // controllo esistenza dell' elemento della dispensa da eliminare
            if(esisteElemDispensa(input.getNome()).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Elemento non trovato");

            dao.eliminaElemDispensaByNome(input.getNome());
        } catch (SQLException e){
            // la delete nel database sollevera' sicuramente un eccezione quindi non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return eliminazioneEffettuata(esitoQuery);
    }

    @DeleteMapping("delete/Dispensa")
    public ResponseEntity<String> deleteDispensa(){
        try{
            DispensaDAO dao = new DispensaDAO(connection);
            dao.eliminaDispensa();
        }catch (SQLException e){
            // la delete nel database sollevera' sicuramente un eccezione quindi non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return eliminazioneEffettuata(esitoQuery);
    }
}
