package de.ifgi.musil.eventDetection.io;

import java.util.List;

import de.ifgi.musil.eventDetection.Event;

/**
 * Simple string writer
 * @author Kiesow
 *
 */
public class TextEventWriter implements IEventWriter {

	public String write(List<Event> events) {
		
		StringBuilder sb = new StringBuilder();
		
		if (events != null) {
			for (int i = 0; i < events.size() - 1; i++) {
				sb.append(events.get(i).toString() + "\n");
			}
			sb.append(events.get(events.size() - 1).toString());
		}
		
		return sb.toString();
	}

}
