package org.example.events;

public class Organizator {
    String emri;
    String kontakti;

    public String getEmri() {
        return emri;
    }

    public void setEmri(String emri) {
        this.emri = emri;
    }

    public String getKontakti() {
        return kontakti;
    }

    public void setKontakti(String kontakti) {
        this.kontakti = kontakti;
    }

    public Organizator(String emri, String kontakti) {
        this.emri = emri;
        this.kontakti = kontakti;
    }
}
