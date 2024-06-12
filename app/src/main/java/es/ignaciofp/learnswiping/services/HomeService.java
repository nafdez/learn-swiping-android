package es.ignaciofp.learnswiping.services;

import es.ignaciofp.learnswiping.models.deck.Deck;

public class HomeService {

    private static HomeService instance;

    private Deck workingDeck;

    private HomeService() {
        workingDeck = null; // Default for not
    }

    public static HomeService getInstance() {
        if (instance == null) instance = new HomeService();
        return instance;
    }

    public Deck getWorkingDeck() {
        return workingDeck;
    }

    public void setWorkingDeck(Deck workingDeck) {
        this.workingDeck = workingDeck;
    }
}
