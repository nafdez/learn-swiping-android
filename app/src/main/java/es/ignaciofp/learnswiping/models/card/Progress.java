package es.ignaciofp.learnswiping.models.card;

import com.google.gson.annotations.SerializedName;

public class Progress {
    @SerializedName("card_id")
    private long cardID;
    private float ease;
    private int interval;
    private int priority;
    @SerializedName("days_hidden")
    private int daysHidden;
    @SerializedName("watch_count")
    private int watchCount;
    @SerializedName("priority_exam")
    private int priorityExam;
    @SerializedName("days_hidden_exam")
    private int daysHiddenExam;
    @SerializedName("answer_count")
    private int answerCount;
    @SerializedName("correct_count")
    private int correctCount;
    @SerializedName("is_relearning")
    private boolean isRelearning;
    @SerializedName("is_buried")
    private boolean isBuried;

    public Progress() {

    }

    public Progress(Progress p) {
        this(p.getCardID(), p.getEase(), p.getInterval(), p.getPriority(), p.getDaysHidden(), p.getWatchCount(), p.getPriorityExam(), p.getDaysHiddenExam(), p.getAnswerCount(), p.getCorrectCount(), p.isRelearning(), p.isBuried());
    }

    public Progress(long cardID, float ease, int interval, int priority, int daysHidden, int watchCount, int priorityExam, int daysHiddenExam, int answerCount, int correctCount, boolean isRelearning, boolean isBuried) {
        this.cardID = cardID;
        this.ease = ease;
        this.interval = interval;
        this.priority = priority;
        this.daysHidden = daysHidden;
        this.watchCount = watchCount;
        this.priorityExam = priorityExam;
        this.daysHiddenExam = daysHiddenExam;
        this.answerCount = answerCount;
        this.correctCount = correctCount;
        this.isRelearning = isRelearning;
        this.isBuried = isBuried;
    }

    public long getCardID() {
        return cardID;
    }

    public void setCardID(long cardID) {
        this.cardID = cardID;
    }

    public float getEase() {
        return ease;
    }

    public void setEase(float ease) {
        this.ease = ease;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getDaysHidden() {
        return daysHidden;
    }

    public void setDaysHidden(int daysHidden) {
        this.daysHidden = daysHidden;
    }

    public int getWatchCount() {
        return watchCount;
    }

    public void setWatchCount(int watchCount) {
        this.watchCount = watchCount;
    }

    public int getPriorityExam() {
        return priorityExam;
    }

    public void setPriorityExam(int priorityExam) {
        this.priorityExam = priorityExam;
    }

    public int getDaysHiddenExam() {
        return daysHiddenExam;
    }

    public void setDaysHiddenExam(int daysHiddenExam) {
        this.daysHiddenExam = daysHiddenExam;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    public boolean isRelearning() {
        return isRelearning;
    }

    public void setRelearning(boolean relearning) {
        isRelearning = relearning;
    }

    public boolean isBuried() {
        return isBuried;
    }

    public void setBuried(boolean buried) {
        isBuried = buried;
    }
}
