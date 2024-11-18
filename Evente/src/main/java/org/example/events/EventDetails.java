package org.example.events;


public abstract class EventDetails {
    int priceDecoration;
    int priceMusic;
    int cmimiTotal;

    public EventDetails(int priceDecoration, int priceMusic) {
        this.priceDecoration = priceDecoration;
        this.priceMusic = priceMusic;
    }

    public int getPriceDecoration() {
        return priceDecoration;
    }

    public int getPriceMusic() {
        return priceMusic;
    }

    public void setPriceDecoration(int priceDecoration) {
        this.priceDecoration = priceDecoration;
    }

    public void setPriceMusic(int priceMusic) {
        this.priceMusic = priceMusic;
    }

    public int addDecoration() {
        cmimiTotal += getPriceDecoration();
        return cmimiTotal;
    }

    public int addMusic() {
        cmimiTotal += getPriceMusic();
        return cmimiTotal;
    }

    public int addBoth() {
        cmimiTotal= cmimiTotal + getPriceDecoration() + getPriceMusic();
        return cmimiTotal;
    }
}

