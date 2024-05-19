package es.ignaciofp.learnswiping.models;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class User {

    @SerializedName("acc_id")
    private long id;
    private String username;
    private String password;
    private String email;
    private String name;
    @SerializedName("pic_id")
    private String picId;
    private String token;
    @SerializedName("last_seen")
    private LocalDateTime lastSeen;
    private LocalDateTime since;

    private List<Deck> ownedDecks;
    private List<Deck> subscribedDecks;

    public User() {
    }

    public User(long id, String username, String password, String email, String name, String picId, String token, LocalDateTime lastSeen, LocalDateTime since) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.picId = picId;
        this.token = token;
        this.lastSeen = lastSeen;
        this.since = since;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }

    public LocalDateTime getSince() {
        return since;
    }

    public void setSince(LocalDateTime since) {
        this.since = since;
    }

    public List<Deck> getOwnedDecks() {
        return Collections.unmodifiableList(ownedDecks);
    }

    public void setOwnedDecks(List<Deck> ownedDecks) {
        this.ownedDecks = ownedDecks;
    }

    public List<Deck> getSubscribedDecks() {
        return Collections.unmodifiableList(subscribedDecks);
    }

    public void setSubscribedDecks(List<Deck> subscribedDecks) {
        this.subscribedDecks = subscribedDecks;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", picId='" + picId + '\'' +
                ", token='" + token + '\'' +
                ", lastSeen=" + lastSeen +
                ", since=" + since +
                ", ownedDecks=" + ownedDecks +
                ", subscribedDecks=" + subscribedDecks +
                '}';
    }
}
