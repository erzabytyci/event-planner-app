package org.example.datas;

import org.example.events.EventDetails;
import org.example.events.NrPjesmarresve;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Invoice extends EventDetails implements NrPjesmarresve {


    private int nrFtuarve;
    private LocalDateTime dataFatures = LocalDateTime.now();
    private double runningTotal = 0;
    private int menuPrice = 0;
    private int decorationPrice = 0;
    private int musicPrice = 0;
    private boolean isPaid = false;

    private List<ReservationDetails> reservationDetailsList = new ArrayList<>();

    public Invoice(int priceDecoration, int priceMusic) {
        super(priceDecoration, priceMusic);
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public void addReservation(ReservationDetails reservation) {
        reservationDetailsList.add(reservation);
        runningTotal += reservation.calculateTotal();
    }

    @Override
    public int numriPjesmarresve() {
        return nrFtuarve;
    }

    public void setNrFtuarve(int nrFtuarve) {
        this.nrFtuarve = nrFtuarve;
    }

    public void setOfferPrice(int price) {
        this.runningTotal = price;
    }

    public void setMenuPrice(int menuPrice) {
        this.menuPrice = menuPrice;
        this.runningTotal += nrFtuarve * menuPrice;
    }

    public void addDecorationPrice() {
        this.runningTotal += 2000;
        this.decorationPrice = 2000;
    }

    public void addMusicPrice() {
        this.runningTotal += 3000;
        this.musicPrice = 3000;
    }

    public void addBothPrice() {
        this.runningTotal += 4000;
        this.decorationPrice = 2000;
        this.musicPrice = 3000;
    }


    public void printInvoice(List<ReservationDetails> reservationDetailsList) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("-----------------------");
        System.out.println("Data e fatures: " + dataFatures.format(formatter));

        double totalSum = 0;
        for (int i = 0; i < reservationDetailsList.size(); i++) {
            ReservationDetails reservation = reservationDetailsList.get(i);

            double reservationTotal = reservation.calculateTotal();
            if (!reservation.isPaid()) {
                totalSum += reservationTotal;
            }

            System.out.println("Rezervimi numer: " + (i + 1));
            System.out.println("Numri i pjesmarresve: " + reservation.getNrFtuarve());
            System.out.println("Menu Price per person: " + reservation.getMenuPrice() + " euro");
            System.out.println("Decoration Price: " + reservation.getDecorationPrice() + " euro");
            System.out.println("Music Price: " + reservation.getMusicPrice() + " euro");
            System.out.println("Offer Price: " + reservation.getOfferPrice() + " euro");

            if (!reservation.isPaid()) {
                System.out.println("Shuma totale per rezervimin: " + reservationTotal + " euro");
            } else {
                System.out.println("Shuma totale per rezervimin: [Paid]");
            }

            System.out.println("-----------------------");
        }

        System.out.println("Shuma totale e te gjitha rezervimeve: " + totalSum + " euro");
        System.out.println("-----------------------");
    }
}

