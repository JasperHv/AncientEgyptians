package nl.vu.cs.ancientegyptiansgame.observer;

import nl.vu.cs.ancientegyptiansgame.listeners.ScoreListener;

import java.util.ArrayList;
import java.util.List;

public class ScoreObserver {

    private int score;
    private final List<ScoreListener> listeners;

    public ScoreObserver() {
        this.score = 0;
        this.listeners = new ArrayList<>();
    }

    public void addListener(ScoreListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ScoreListener listener) {
        listeners.remove(listener);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int newScore) {
        if (this.score != newScore) {
            this.score = newScore;
            notifyListeners();
        }
    }

    private void notifyListeners() {
        for (ScoreListener listener : listeners) {
            listener.changed(score);
        }
    }
}
