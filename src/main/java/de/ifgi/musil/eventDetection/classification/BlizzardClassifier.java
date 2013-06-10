package de.ifgi.musil.eventDetection.classification;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import de.ifgi.musil.eventDetection.Event;
import de.ifgi.musil.eventDetection.processSimulation.AFocus;

/**
 * Blizzard classifier, checking minimum duration of a potential {@link Event
 * event}.
 * 
 * @author Kiesow
 * 
 */
public class BlizzardClassifier implements IEventClassifier {

	private Duration minDuration;

	// CONSTRUCTORS

	/**
	 * constructor with arbitrary minimum duration
	 * 
	 * @param minDuration
	 *            minimum duration
	 */
	public BlizzardClassifier(Duration minDuration) {
		this.minDuration = minDuration;
	}

	// PUBLIC METHODS

	/**
	 * returns only classified events
	 */
	public List<Event> getClassifiedEvents(List<Event> events) {

		if (events == null)
			new ArrayList<Event>(0);

		List<Event> classified = new ArrayList<Event>(events.size());

		for (Event e : events) {
			classifyEvent(e);
			if (e.getClassified())
				classified.add(e);
		}
		return classified;
	}

	/**
	 * classifies a set of events
	 */
	public void classifyEvents(List<Event> events) {

		for (Event e : events) {
			classifyEvent(e);
		}
	}

	/**
	 * classifies a single event
	 */
	public void classifyEvent(Event event) {

		DateTime tFirst = null;
		DateTime tLast = null;
		DateTime tmp;

		for (AFocus focus : event.getFoci()) {
			tmp = focus.getTimestamp();

			if (tFirst == null || tLast == null) {
				tFirst = tmp;
				tLast = tmp;
			} else {
				if (tmp.isBefore(tFirst)) {
					tFirst = tmp;
				} else if (tmp.isAfter(tLast)) {
					tLast = tmp;
				}
			}
		}
		Duration eDuration = new Duration(tFirst, tLast);

		if (eDuration.isShorterThan(minDuration)) {
			event.setClassified(false);
		} else {
			event.setClassified(true);
		}
	}

}
