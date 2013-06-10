package de.ifgi.musil.eventDetection.individuation;

import java.util.List;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Pseudograph;

import de.ifgi.musil.eventDetection.Event;
import de.ifgi.musil.eventDetection.processSimulation.AFocus;

/**
 * Constructor classes create {@link Event events} by finding the connected
 * components of a process graph.
 * 
 * @author Kiesow
 * 
 */
public interface IEventConstructor {

	public List<Event> separateEvents(
			Pseudograph<AFocus, DefaultEdge> processGraph);
}
