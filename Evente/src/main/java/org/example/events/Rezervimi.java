package org.example.events;

import java.time.LocalDate;


public class Rezervimi implements NrPjesmarresve {
    private int id;
    private int userId;
    private String emriRezervatorit;
    private String emriEventit;
    private double kohezgjatjaEventit;
    private LocalDate dataRezervimit;
    private int numriPresonave;
    private LocalDate dataEventit;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getNumriPresonave() {
        return numriPresonave;
    }

    public void setNumriPresonave(int numriPresonave) {
        this.numriPresonave = numriPresonave;
    }

    public String getEmriRezervatorit() {
        return emriRezervatorit;
    }

    public void setEmriRezervatorit(String emriRezervatorit) {
        this.emriRezervatorit = emriRezervatorit;
    }

    public String getEmriEventit() {
        return emriEventit;
    }

    public void setEmriEventit(String emriEventit) {
        this.emriEventit = emriEventit;
    }

    public double getKohezgjatjaEventit() {
        return kohezgjatjaEventit;
    }

    public void setKohezgjatjaEventit(double kohezgjatjaEventit) {
        this.kohezgjatjaEventit = kohezgjatjaEventit;
    }


    public LocalDate getDataEventit() {
        return dataEventit;
    }

    public void setDataEventit(LocalDate dataEventit) {
        this.dataEventit = dataEventit;
    }

    public LocalDate getDataRezervimit() {
        return dataRezervimit;
    }

    public void setDataRezervimit(LocalDate dataRezervimit) {
        this.dataRezervimit = dataRezervimit;
    }

    @Override
    public int numriPjesmarresve() {
        return numriPresonave;
    }

    @Override
    public String toString() {
        return "Rezervimi{" +
                "id=" + id +
                ", userId=" + userId +
                ", emriRezervatorit='" + emriRezervatorit + '\'' +
                ", emriEventit='" + emriEventit + '\'' +
                ", kohezgjatjaEventit=" + kohezgjatjaEventit +
                ", dataRezervimit=" + dataRezervimit +
                ", numriPresonave=" + numriPresonave +
                ", dataEventit=" + dataEventit +
                '}';
    }
}