package es.ignaciofp.learnswiping.models.deck;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class Deck {

    @SerializedName("deck_id")
    private long ID;
    @SerializedName("owner")
    private long ownerID;
    private String title;
    private String description;
    @SerializedName("pic_id")
    private String picID;
    private boolean visible;
    @SerializedName("updated_at")
    private LocalDateTime updatedAt;
    @SerializedName("created_at")
    private LocalDateTime createdAt;

    public Deck() {}

    public Deck(long ID, long ownerID, String title, String description, String picID, boolean visible, LocalDateTime updatedAt, LocalDateTime createdAt) {
        this.ID = ID;
        this.ownerID = ownerID;
        this.title = title;
        this.description = description;
        this.picID = picID;
        this.visible = visible;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
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

    @Override
    public String toString() {
        return "Deck{" +
                "ID=" + ID +
                ", ownerID=" + ownerID +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", visible=" + visible +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                '}';
    }
}
