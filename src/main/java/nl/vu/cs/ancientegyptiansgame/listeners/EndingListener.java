package nl.vu.cs.ancientegyptiansgame.listeners;

import nl.vu.cs.ancientegyptiansgame.data.model.Ending;

public interface EndingListener {
    /****
 * Called when an ending event is triggered.
 *
 * @param ending the Ending object representing the triggered ending event
 */
void onEndingTriggered(Ending ending);
}