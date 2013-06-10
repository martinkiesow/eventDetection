package de.ifgi.musil.eventDetection;

import java.util.Collections;
import java.util.List;

import de.ifgi.musil.eventDetection.processSimulation.AFocus;

/**
 * A general event, consisting of {@link AFocus foci}; might not be classified
 * as proper event yet.
 * 
 * @author Kiesow
 * 
 */
public class Event {

	private List<AFocus> foci;
	private boolean partial;
	private boolean classified;

	// CONSTRUCTORS

	public Event(List<AFocus> foci) {
		this(foci, false, false);
	}

	public Event(List<AFocus> foci, boolean partial) {
		this(foci, partial, false);
	}

	private Event(List<AFocus> foci, boolean partial, boolean classified) {
		this.foci = foci;
		this.partial = partial;
		this.classified = classified;
	}

	// PUBLIC METHODS

	public String toString() {

		Collections.sort(foci);
		StringBuilder sb = new StringBuilder();

		if (partial)
			sb.append("Partial Event");
		else
			sb.append("Event");

		sb.append("(");
		if (foci.size() > 1) {
			sb.append(foci.get(0).getTimestamp() + " - "
					+ foci.get(foci.size() - 1).getTimestamp().toString());
		} else if (foci.size() == 1) {
			sb.append(foci.get(0).getTimestamp());
		}

		sb.append(")");

		// add a list of all contained foci
		// sb.append("\n");
		// for (AFocus f : foci) {
		// sb.append(f.toString() + "\n");
		// }
		// sb.append("\n");

		return sb.toString();
	}

	// GETTERS & SETTERS

	public List<AFocus> getFoci() {
		return foci;
	}

	public boolean getPartial() {
		return partial;
	}

	public boolean getClassified() {
		return classified;
	}

	public void setClassified(boolean classified) {
		this.classified = classified;
	}
}
