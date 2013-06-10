package de.ifgi.musil.eventDetection.classification;

import java.util.List;

import de.ifgi.musil.eventDetection.Event;

/**
 * Classifier classes check case specific conditions after a potential
 * {@link Event event} is found.
 * 
 * @author Kiesow
 * 
 */
public interface IEventClassifier {

	public void classifyEvents(List<Event> events);

	public void classifyEvent(Event event);

	public List<Event> getClassifiedEvents(List<Event> events);
}
