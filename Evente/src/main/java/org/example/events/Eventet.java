package org.example.events;

public class Eventet {

    NrPjesmarresve nrPjesmarresve;
    Organizator[] organizators;

    public Eventet(NrPjesmarresve nrPjesmarresve, Organizator[] organizators) {
        this.nrPjesmarresve = nrPjesmarresve;
        this.organizators = organizators;
    }
    public void displayEventet() {
        System.out.println("------------------------");
        System.out.println("Detajet e eventeve: ");
        System.out.println("Salla e rezervuar me: " + nrPjesmarresve.numriPjesmarresve() + " persona");
        System.out.println("Me organizator: ");

        for (Organizator organizator : organizators) {
            System.out.println("Emri: " + organizator.getEmri());
            System.out.println("Numri telefonit: " + organizator.getKontakti());
        }
        System.out.println("------------------------");
    }

}
