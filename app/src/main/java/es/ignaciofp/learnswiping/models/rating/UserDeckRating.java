package es.ignaciofp.learnswiping.models.rating;

import com.google.gson.annotations.SerializedName;

public class UserDeckRating {

    private final float rating;

    public UserDeckRating(int rating) {
        this.rating = (rating * 5) / 100f; // Converts from a 0-100 range to a 0-5
    }

    public float getRating() {
        return rating;
    }
}
