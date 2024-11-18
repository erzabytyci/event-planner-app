package org.example.datas;

public class ReservationDetails {

    private int id;
    private int nrFtuarve;
    private int menuPrice;
    private int decorationPrice;
    private int musicPrice;
    private int offerPrice;
    private boolean isOffer;
    private boolean isPaid;
    private int userId;
    private double total_amount;

    public ReservationDetails(int nrFtuarve, int menuPrice, int decorationPrice, int musicPrice, int offerPrice, boolean isOffer, int userId) {
        this.nrFtuarve = nrFtuarve;
        this.menuPrice = menuPrice;
        this.decorationPrice = decorationPrice;
        this.musicPrice = musicPrice;
        this.offerPrice = offerPrice;
        this.isOffer = isOffer;
        this.userId = userId;
        this.isPaid = false;
        this.total_amount = calculateTotal();
    }

    public int getNrFtuarve() {
        return nrFtuarve;
    }

    public void setNrFtuarve(int nrFtuarve) {
        this.nrFtuarve = nrFtuarve;
    }

    public int getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(int menuPrice) {
        this.menuPrice = menuPrice;
    }

    public int getDecorationPrice() {
        return decorationPrice;
    }

    public void setDecorationPrice(int decorationPrice) {
        this.decorationPrice = decorationPrice;
    }

    public int getMusicPrice() {
        return musicPrice;
    }

    public void setMusicPrice(int musicPrice) {
        this.musicPrice = musicPrice;
    }

    public int getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(int offerPrice) {
        this.offerPrice = offerPrice;
    }

    public boolean isOffer() {
        return isOffer;
    }

    public void setOffer(boolean offer) {
        isOffer = offer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public double calculateTotal() {
        return isOffer ? offerPrice + decorationPrice + musicPrice : (nrFtuarve * menuPrice) + decorationPrice + musicPrice;
    }
}
