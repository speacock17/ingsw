package ingsw.server.entityDTO.menuDTO;

public class ElemMenuFormDTO {
    private String nome;
    private String descrizione;
    private Float costo;
    private String allergeni;
    private String categoria;
    private Integer postoCategoria;

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public Float getCosto() {
        return costo;
    }

    public String getAllergeni() {
        return allergeni;
    }

    public String getCategoria() {
        return categoria;
    }

    public Integer getPostoCategoria() {
        return postoCategoria;
    }
}
