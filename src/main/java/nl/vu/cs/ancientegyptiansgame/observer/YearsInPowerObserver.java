package nl.vu.cs.ancientegyptiansgame.observer;

import nl.vu.cs.ancientegyptiansgame.listeners.YearsInPowerListener;

import java.util.ArrayList;
import java.util.List;

public class YearsInPowerObserver {

    private int yearsInPower;
    private final List<YearsInPowerListener> listeners;

    /**
     * Constructs a YearsInPowerObserver with the years in power initialized to zero and no registered listeners.
     */
    public YearsInPowerObserver() {
        this.yearsInPower = 0;
        this.listeners = new ArrayList<>();
    }

    /****
     * Registers a listener to be notified when the years in power value changes.
     *
     * @param listener the YearsInPowerListener to add
     */
    public void addListener(YearsInPowerListener listener) {
        listeners.add(listener);
    }

    /****
     * Unregisters a listener so it will no longer receive updates when the years in power changes.
     *
     * @param listener the listener to remove
     */
    public void removeListener(YearsInPowerListener listener) {
        listeners.remove(listener);
    }

    /**
     * Returns the current number of years in power.
     *
     * @return the current value of years in power
     */
    public int getYearsInPower() {
        return yearsInPower;
    }

    /****
     * Updates the years in power and notifies all registered listeners if the value has changed.
     *
     * @param newValue the new value for years in power
     */
    public void setYearsInPower(int newValue) {
        if (this.yearsInPower != newValue) {
            this.yearsInPower = newValue;
            notifyListeners();
        }
    }

    /**
     * Notifies all registered listeners of the current years in power value.
     *
     * Invokes the {@code changedYears(int)} method on each listener with the updated value.
     */
    private void notifyListeners() {
        for (YearsInPowerListener listener : listeners) {
            listener.changedYears(yearsInPower);
        }
    }
}
