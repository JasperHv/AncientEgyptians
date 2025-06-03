package nl.vu.cs.ancientegyptiansgame.observer;

import nl.vu.cs.ancientegyptiansgame.listeners.YearsInPowerListener;

import java.util.ArrayList;
import java.util.List;

public class YearsInPowerObserver {

    private int yearsInPower;
    private final List<YearsInPowerListener> listeners;

    public YearsInPowerObserver() {
        this.yearsInPower = 0;
        this.listeners = new ArrayList<>();
    }

    public void addListener(YearsInPowerListener listener) {
        listeners.add(listener);
    }

    public void removeListener(YearsInPowerListener listener) {
        listeners.remove(listener);
    }

    public int getYearsInPower() {
        return yearsInPower;
    }

    public void setYearsInPower(int newValue) {
        if (this.yearsInPower != newValue) {
            this.yearsInPower = newValue;
            notifyListeners();
        }
    }

    private void notifyListeners() {
        for (YearsInPowerListener listener : listeners) {
            listener.changedYears(yearsInPower);
        }
    }
}
