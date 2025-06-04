package nl.vu.cs.ancientegyptiansgame.observer;

import nl.vu.cs.ancientegyptiansgame.listeners.ScoreListener;

import java.util.ArrayList;
import java.util.List;

public class ScoreObserver {

    private int score;
    private final List<ScoreListener> listeners;

    /**
     * Constructs a ScoreObserver with an initial score of zero and no registered listeners.
     */
    public ScoreObserver() {
        this.score = 0;
        this.listeners = new ArrayList<>();
    }

    /**
     * Registers a new listener to receive score change notifications.
     *
     * @param listener the listener to be added
     */
    public void addListener(ScoreListener listener) {
        listeners.add(listener);
    }

    /**
     * Unregisters a listener so it will no longer receive score updates.
     *
     * @param listener the listener to remove
     */
    public void removeListener(ScoreListener listener) {
        listeners.remove(listener);
    }

    /**
     * Returns the current score.
     *
     * @return the current score value
     */
    public int getScore() {
        return score;
    }

    /**
     * Updates the score if the new value differs from the current score and notifies all registered listeners of the change.
     *
     * @param newScore the new score to set
     */
    public void setScore(int newScore) {
        if (this.score != newScore) {
            this.score = newScore;
            notifyListeners();
        }
    }

    /**
     * Notifies all registered listeners of a score change by invoking their {@code changedScore} method with the current score.
     */
    private void notifyListeners() {
        for (ScoreListener listener : listeners) {
            listener.changedScore(score);
        }
    }
}
