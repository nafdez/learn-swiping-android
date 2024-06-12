package es.ignaciofp.learnswiping.models.deck;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

import es.ignaciofp.learnswiping.models.rating.Rating;
import es.ignaciofp.learnswiping.models.rating.UserDeckRating;

public class DeckDetails {

    private String title;
    private String description;
    @SerializedName("pic_id")
    private String picID;
    @SerializedName("updated_at")
    private LocalDateTime updatedAt;
    @SerializedName("created_at")
    private LocalDateTime createdAt;
    private boolean isSusbcribed;
    private long subscriptions;

    @SerializedName("total_progress")
    private int totalProgress;
    @SerializedName("cards_revised")
    private int cardsRevised;
    @SerializedName("cards_remaining")
    private int cardsRemaining;

    @SerializedName("owner_id")
    private long ownerID;
    @SerializedName("owner")
    private String ownerUsername;

    private Bitmap picture;
    private Rating rating;
    private UserDeckRating personalRanking;


    public DeckDetails(String title, String description, String picID, LocalDateTime updatedAt, LocalDateTime createdAt, boolean isSusbcribed, long subscriptions, int totalProgress, int cardsRevised, int cardsRemaining, long ownerID, String ownerUsername) {
        this.title = title;
        this.description = description;
        this.picID = picID;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.isSusbcribed = isSusbcribed;
        this.subscriptions = subscriptions;
        this.totalProgress = totalProgress;
        this.cardsRevised = cardsRevised;
        this.cardsRemaining = cardsRemaining;
        this.ownerID = ownerID;
        this.ownerUsername = ownerUsername;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicID() {
        return picID;
    }

    public void setPicID(String picID) {
        this.picID = picID;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isSusbcribed() {
        return isSusbcribed;
    }

    public void setSusbcribed(boolean susbcribed) {
        isSusbcribed = susbcribed;
    }

    public long getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(long subscriptions) {
        this.subscriptions = subscriptions;
    }

    public int getTotalProgress() {
        return totalProgress;
    }

    public void setTotalProgress(int totalProgress) {
        this.totalProgress = totalProgress;
    }

    public int getCardsRevised() {
        return cardsRevised;
    }

    public void setCardsRevised(int cardsRevised) {
        this.cardsRevised = cardsRevised;
    }

    public int getCardsRemaining() {
        return cardsRemaining;
    }

    public void setCardsRemaining(int cardsRemaining) {
        this.cardsRemaining = cardsRemaining;
    }

    public long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }
}
