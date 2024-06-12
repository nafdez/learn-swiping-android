package es.ignaciofp.learnswiping.models.card;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Card {

    @SerializedName("card_id")
    private long cardID;
    @SerializedName("deck_id")
    private long deckID;
    private String title;
    private String front;
    private String back;
    private String question;
    private String answer;
    @SerializedName("wrong")
    private List<WrongAnswer> wrongAnswers;

    public Card() {

    }

    public Card(long cardID, long deckID, String title, String front, String back, String question, String answer, List<WrongAnswer> wrongAnswers) {
        this.cardID = cardID;
        this.deckID = deckID;
        this.title = title;
        this.front = front;
        this.back = back;
        this.question = question;
        this.answer = answer;
        this.wrongAnswers = wrongAnswers;
    }

    public long getCardID() {
        return cardID;
    }

    public void setCardID(long cardID) {
        this.cardID = cardID;
    }

    public long getDeckID() {
        return deckID;
    }

    public void setDeckID(long deckID) {
        this.deckID = deckID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<WrongAnswer> getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(List<WrongAnswer> wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }

    public static class WrongAnswer {
        @SerializedName("wrong_id")
        private long wrongID;
        private String answer;

        public WrongAnswer(String answer) {
            this(0, answer);
        }

        public WrongAnswer(long wrongID, String answer) {
            this.wrongID = wrongID;
            this.answer = answer;
        }

        public long getWrongID() {
            return wrongID;
        }

        public void setWrongID(long wrongID) {
            this.wrongID = wrongID;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }
}
