package br.com.aguido.financas.model;

import java.io.Serializable;

/**
 * Created by Inmetrics on 15/09/2016.
 */
public class Despesa implements Serializable{

    private long id;
    private String tituloDespesa;               //Aluguel
    private String valorDespesa;                //R$ 3.652,24
    private String dataDespesa;                 //25/09/2016
    private String tipoDespesa;                 //fixo (todo_ dia xx durante xx meses) ou instantanea (só no dia xx do mes atual)
    private String imagemDespesa;               //Presente, Banco, Apartamento, Casa, Igreja, Inesperado

    public Despesa(){

    }

    public Despesa(String tituloDespesa, String valorDespesa, String dataDespesa, String tipoDespesa, String imagemDespesa){
        this.tituloDespesa = tituloDespesa;
        this.valorDespesa = valorDespesa;
        this.dataDespesa = dataDespesa;
        this.tipoDespesa = tipoDespesa;
        this.imagemDespesa = imagemDespesa;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTituloDespesa() {
        return tituloDespesa;
    }

    public void setTituloDespesa(String tituloDespesa) {
        this.tituloDespesa = tituloDespesa;
    }

    public String getValorDespesa() {
        return valorDespesa;
    }

    public void setValorDespesa(String valorDespesa) {
        this.valorDespesa = valorDespesa;
    }

    public String getDataDespesa() {
        return dataDespesa;
    }

    public void setDataDespesa(String dataDespesa) {
        this.dataDespesa = dataDespesa;
    }

    public String getTipoDespesa() {
        return tipoDespesa;
    }

    public void setTipoDespesa(String tipoDespesa) {
        this.tipoDespesa = tipoDespesa;
    }

    public String getImagemDespesa() {
        return imagemDespesa;
    }

    public void setImagemDespesa(String imagemDespesa) {
        this.imagemDespesa = imagemDespesa;
    }
}
