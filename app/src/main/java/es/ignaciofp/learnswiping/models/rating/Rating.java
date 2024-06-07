package es.ignaciofp.learnswiping.models.rating;

import com.google.gson.annotations.SerializedName;

public class Rating {
    @SerializedName("rating_count")
    private int count;
    @SerializedName("avg_rating")
    private float avg;

    public Rating(int count, float avg) {
        this.count = count;
        this.avg = (avg * 5) / 100f;
    }

    public int getCount() {
        return count;
    }

    public float getAvg() {
        return avg;
    }
}
