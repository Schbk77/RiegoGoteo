package com.example.serj.riegogoteo.clases;

import java.util.Calendar;

public class Lectura {

    private Calendar fecha, hi, hf;
    private double contadorInicial, contadorFinal, metrosCubicosTeoricos;
    private String zona, idsector, sector;
    private int olivos;

    public Lectura(Calendar fecha, Calendar hi, Calendar hf, double contadorInicial, double contadorFinal, double metrosCubicosTeoricos, String zona, String idsector, String sector, int olivos) {
        this.fecha = fecha;
        this.hi = hi;
        this.hf = hf;
        this.contadorInicial = contadorInicial;
        this.contadorFinal = contadorFinal;
        this.metrosCubicosTeoricos = metrosCubicosTeoricos;
        this.zona = zona;
        this.idsector = idsector;
        this.sector = sector;
        this.olivos = olivos;
    }

    public Calendar getFecha() {
        return fecha;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }

    public Calendar getHi() {
        return hi;
    }

    public void setHi(Calendar hi) {
        this.hi = hi;
    }

    public Calendar getHf() {
        return hf;
    }

    public void setHf(Calendar hf) {
        this.hf = hf;
    }

    public double getContadorInicial() {
        return contadorInicial;
    }

    public void setContadorInicial(double contadorInicial) {
        this.contadorInicial = contadorInicial;
    }

    public double getContadorFinal() {
        return contadorFinal;
    }

    public void setContadorFinal(double contadorFinal) {
        this.contadorFinal = contadorFinal;
    }

    public double getMetrosCubicosTeoricos() {
        return metrosCubicosTeoricos;
    }

    public void setMetrosCubicosTeoricos(double metrosCubicosTeoricos) {
        this.metrosCubicosTeoricos = metrosCubicosTeoricos;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getIdsector() {
        return idsector;
    }

    public void setIdsector(String idsector) {
        this.idsector = idsector;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public int getOlivos() {
        return olivos;
    }

    public void setOlivos(int olivos) {
        this.olivos = olivos;
    }
}
