package es.ignaciofp.learnswiping.services;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.managers.CardManager;
import es.ignaciofp.learnswiping.models.card.Card;

public class StudyService {

    private static StudyService instance;

    private final CardManager CARD_MANAGER = CardManager.getInstance();

    private List<Card> currentList;
    private int currentPosition = -1;


    private StudyService() {
    }

    public static StudyService getInstance() {
        if (instance == null) instance = new StudyService();
        return instance;
    }

    public void feedCardList(List<Card> cards) {
        currentList = new ArrayList<>(cards);
        currentPosition = -1;
    }

    public Card next() {
        // TODO: Pre-load 3 or 5 more cards and get from there
        if (currentList == null || currentPosition >= currentList.size() - 1) return null;
        return currentList.get(++currentPosition);
    }

    public Card previous() {
        if (currentList == null || currentPosition == 0) return null;
        return currentList.get(--currentPosition);
    }


}