package ingsw.server.entityController;

import ingsw.server.entityDAO.CategoriaDAO;
import ingsw.server.entityDTO.categoriaDTO.CategFormDTO;
import ingsw.server.entityDTO.categoriaDTO.ModNomeCategDTO;
import ingsw.server.entityDTO.categoriaDTO.ModPostoMenuCategDTO;
import ingsw.server.entityDTO.categoriaDTO.NomeCategDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
@RequestMapping("controller/categoria")
public class CategoriaController extends SuperController{
    public CategoriaController(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    private String esisteCategoria(String nome){
        try {
            // accedo alla DAO per cercare la categoria
            CategoriaDAO dao = new CategoriaDAO(connection);

            ResultSet rs = dao.recuperaCategByNome(nome);
            if(rs == null)
                return FALLIMENTO;
            // estrapolo solo la colonna Nome
            String nomeDaRS = ottieniColonnaDaResultSetSingolaRiga(rs, 1);
            if(nomeDaRS == null)
                return FALLIMENTO;
            else{
                if(nomeDaRS.equals(nome)) return nome;
                else return FALLIMENTO;
            }
        }catch (SQLException e){
            return super.handleSQLException(e);
        }
    }

    @PostMapping("registra/inOrder")
    public ResponseEntity<String> inserisciCategInOrder(@RequestBody NomeCategDTO input){
        try {
            // accedo alla DAO delle categorie
            CategoriaDAO dao = new CategoriaDAO(connection);

            // controllo che una categoria con lo stesso nome non esista gia
            if(esisteCategoria(input.getNome()).equals(FALLIMENTO)){
                // se non esiste gia una categoria ricavo l'ultimo posto
                ResultSet rs = dao.recuperaUltimoPosto();

                // estraggo il risultato della query
                String ultimoPosto = ottieniColonnaDaResultSetSingolaRiga(rs, 2);

                if(ultimoPosto == null)
                    // se non ci sono categorie inserite, la inserisco in prima posizione
                    dao.inserisciCategoria(input.getNome(), 1);

                // se qualcosa e' andato storto esco
                if(ultimoPosto.equals(FALLIMENTO))
                    return ResponseEntity.badRequest().body("Errore nel recupero ultima posizione");

                // ricavo il valore effettivo dell' ultimo posto
                Integer ultimoPostoInt = Integer.valueOf(ultimoPosto);

                // inserisco i dati
                dao.inserisciCategoria(input.getNome(), ultimoPostoInt + 1);
            }
            else        // se esiste una categoria con questo nome
                return ResponseEntity.badRequest().body("Categoria gia esistente");
        } catch (SQLException e) {
            // la insert nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return registrazioneEffettuata(esitoQuery);
    }

    @PostMapping("registra/generico")
    public ResponseEntity<String> inserisciCategGenerica(@RequestBody CategFormDTO input){
        try {
            // accedo alla DAO delle categorie
            CategoriaDAO dao = new CategoriaDAO(connection);

            // controllo che una categoria con lo stesso nome non esista gia
            if(esisteCategoria(input.getNome()).equals(FALLIMENTO))
                // inserisco i dati
                dao.inserisciCategoria(input.getNome(), input.getPostoMenu());
            else        // se esiste una categoria con questo nome
                return ResponseEntity.badRequest().body("Categoria gia esistente");
        } catch (SQLException e) {
            // la insert nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return registrazioneEffettuata(esitoQuery);
    }

    @PatchMapping("update/nome")
    public ResponseEntity<String> updateNomeCateg(@RequestBody ModNomeCategDTO input){
        try{
            // accedo alla dao per modificare il nome della categoria
            CategoriaDAO dao = new CategoriaDAO(connection);

            // controllo che la categoria esista
            if(esisteCategoria(input.getNome()).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Categoria non trovata");

            // controllo che una categoria con lo stesso nome non esista gia
            if(esisteCategoria(input.getNewName()).equals(FALLIMENTO))
                //modifico il nome
                dao.modificaNomeCateg(input.getNome(), input.getNewName());
            else        // se esiste gia una categoria con questo nome
                return ResponseEntity.badRequest().body("Categoria gia esistente");

        } catch(SQLException e){
            // la modifica nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return modificaEffettuata(esitoQuery);
    }

    @PatchMapping("update/postoMenu")
    public ResponseEntity<String> updatePostoMenuCateg(@RequestBody ModPostoMenuCategDTO input){
        try {
            // accedo alla dao per modificare il posto del menu
            CategoriaDAO dao = new CategoriaDAO(connection);

            // controllo che la categoria esista
            if(esisteCategoria(input.getNome()).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Categoria non trovata");

            dao.modificaPostoCateg(input.getNome(), input.getPostoMenu());
        } catch(SQLException e){
            // la modifica nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return modificaEffettuata(esitoQuery);
    }

    @DeleteMapping("delete/categoria")
    public ResponseEntity<String> deleteCategByNome(@RequestBody NomeCategDTO input){
        try {
            // accedo alla dao per modificare il posto del menu
            CategoriaDAO dao = new CategoriaDAO(connection);

            // controllo che la categoria esista
            if(esisteCategoria(input.getNome()).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Categoria non trovata");

            dao.eliminaCategByNome(input.getNome());
        }catch(SQLException e){
            // la delete nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return eliminazioneEffettuata(esitoQuery);
    }

    @DeleteMapping("delete/tutteCategorie")
    public ResponseEntity<String> deleteTutteCateg(){
        try {
            // accedo alla dao per modificare il posto del menu
            CategoriaDAO dao = new CategoriaDAO(connection);

            dao.eliminaCategorie();
        }catch(SQLException e){
            // la delete nel database sollevera' sicuramente un eccezione quindi qui non faccio nulla
            esitoQuery = super.handleSQLException(e);
        }
        // qui effettivamente gestisco l'eccezione (inevitabile)
        return eliminazioneEffettuata(esitoQuery);
    }

    @GetMapping("recupera/categoria")
    public ResponseEntity<String> recuperaCategoriaByNome(@RequestBody NomeCategDTO input){
        // restituisce una stringa elencata di nome e posto nel menu di una categoria precisa
        try {
            CategoriaDAO dao = new CategoriaDAO(connection);

            // controllo che esiste la categoria
            if(esisteCategoria(input.getNome()).equals(FALLIMENTO))
                return ResponseEntity.badRequest().body("Categoria non trovata");

            ResultSet rs = dao.recuperaCategByNome(input.getNome());
            return ResponseEntity.status(HttpStatus.OK).body(componiDaResultSet(rs));
        }catch (SQLException e){
            esitoQuery = super.handleSQLException(e);
            return ResponseEntity.badRequest().body(esitoQuery);
        }
    }

    @GetMapping("recupera/tutteCategorie")
    public ResponseEntity<String> recuperaTutteCateg(){
        // restituisce una stringa elencata di nome e posto nel menu di TUTTE le categorie
        try {
            CategoriaDAO dao = new CategoriaDAO(connection);

            ResultSet rs = dao.recuperaTutteCateg();
            return ResponseEntity.status(HttpStatus.OK).body(componiDaResultSet(rs));
        }catch (SQLException e){
            esitoQuery = super.handleSQLException(e);
            return ResponseEntity.badRequest().body(esitoQuery);
        }
    }
}
