package com.anb;

/**
 * Created with IntelliJ IDEA.
 * User: davidolori
 * Date: 1/13/15
 * Time: 11:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class Maquinaria {

    private String marca;
    private String clase;
    private String modelo;

    private String ESP_ENGINE;
    private String ESP_HP;
    private String ESP_TRANS;
    private String ESP_WEIGTH;
    private String ESP_CARRIER;

    private String pricing_year;
    private String pricing_retail;
    private String pricing_loan;

    private String sn_year;
    private String sn_beginning;

    private String descripcion;

    public String getESP_CARRIER() {
        return ESP_CARRIER;
    }

    public void setESP_CARRIER(String ESP_CARRIER) {
        this.ESP_CARRIER = ESP_CARRIER;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getSn_year() {
        return sn_year;
    }

    public void setSn_year(String sn_year) {
        this.sn_year = sn_year;
    }

    public String getSn_beginning() {
        return sn_beginning;
    }

    public void setSn_beginning(String sn_beginning) {
        this.sn_beginning = sn_beginning;
    }

    public String getPricing_year() {
        return pricing_year;
    }

    public void setPricing_year(String pricing_year) {
        this.pricing_year = pricing_year;
    }

    public String getPricing_retail() {
        return pricing_retail;
    }

    public void setPricing_retail(String pricing_retail) {
        this.pricing_retail = pricing_retail;
    }

    public String getPricing_loan() {
        return pricing_loan;
    }

    public void setPricing_loan(String pricing_loan) {
        this.pricing_loan = pricing_loan;
    }



    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getESP_ENGINE() {
        return ESP_ENGINE;
    }

    public void setESP_ENGINE(String ESP_ENGINE) {
        this.ESP_ENGINE = ESP_ENGINE;
    }

    public String getESP_HP() {
        return ESP_HP;
    }

    public void setESP_HP(String ESP_HP) {
        this.ESP_HP = ESP_HP;
    }

    public String getESP_TRANS() {
        return ESP_TRANS;
    }

    public void setESP_TRANS(String ESP_TRANS) {
        this.ESP_TRANS = ESP_TRANS;
    }

    public String getESP_WEIGTH() {
        return ESP_WEIGTH;
    }

    public void setESP_WEIGTH(String ESP_WEIGTH) {
        this.ESP_WEIGTH = ESP_WEIGTH;
    }
}
