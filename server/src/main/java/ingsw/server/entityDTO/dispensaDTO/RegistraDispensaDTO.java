package ingsw.server.entityDTO.dispensaDTO;

public class RegistraDispensaDTO {
    private String nome;
    private String descrizione;
    private Float costoAcq;
    private String unitaDiMisura;
    private Float quantita;
    private Float sogliaCritica;

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public Float getCostoAcq() {
        return costoAcq;
    }

    public String getUnitaDiMisura() {
        return unitaDiMisura;
    }

    public Float getQuantita() {
        return quantita;
    }

    public Float getSogliaCritica() {
        return sogliaCritica;
    }
}
