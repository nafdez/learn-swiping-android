package es.ignaciofp.learnswiping.services;

public class HomeService {

    private static HomeService instance;

    private int workingDeckID;

    public static final int CLEAR_SELECTION = -1;

    private HomeService() {
        workingDeckID = CLEAR_SELECTION; // Default for not
    }

    public static HomeService getInstance() {
        if (instance == null) instance = new HomeService();
        return instance;
    }

    public int getWorkingDeckID() {
        return workingDeckID;
    }

    public void setWorkingDeckID(int workingDeckID) {
        this.workingDeckID = workingDeckID;
    }
}
