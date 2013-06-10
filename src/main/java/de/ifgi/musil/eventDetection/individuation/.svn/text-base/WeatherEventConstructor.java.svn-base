package de.ifgi.musil.eventDetection.individuation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Pseudograph;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import de.ifgi.musil.eventDetection.Event;
import de.ifgi.musil.eventDetection.processSimulation.AFocus;

/**
 * Specific event constructor, separating {@link Event events} and preparing
 * classification.
 * 
 * @author Kiesow
 * 
 */
public class WeatherEventConstructor implements IEventConstructor {

	DateTime firstComplete;
	DateTime lastComplete;

	/**
	 * constructor
	 * 
	 * @param foci
	 *            list of original input foci to determine partial/complete
	 *            events
	 * @param temporalRange
	 *            temporal range to determine partial/complete events
	 * @throws Exception
	 *             if foci is empty
	 */
	public WeatherEventConstructor(ArrayList<AFocus> foci,
			Duration temporalRange) throws Exception {

		// calculate temporal thresholds for complete events
		if (foci != null && foci.size() > 0) {

			Collections.sort(foci);
			firstComplete = foci.get(0).getTimestamp().plus(temporalRange);
			lastComplete = foci.get(foci.size() - 1).getTimestamp()
					.minus(temporalRange);
		} else {
			throw new Exception("No input foci given.");
		}
	}

	/**
	 * separates events as connected components from the process graph; decides
	 * whether an event is complete or if it might be cut by the temporal extent
	 * of the input data
	 */
	public List<Event> separateEvents(
			Pseudograph<AFocus, DefaultEdge> processGraph) {

		ConnectivityInspector<AFocus, DefaultEdge> conInspector = new ConnectivityInspector<AFocus, DefaultEdge>(
				processGraph);
		List<Set<AFocus>> conComps = conInspector.connectedSets();

		ArrayList<Event> events = new ArrayList<Event>(conComps.size());
		boolean partial = false;

		for (Set<AFocus> set : conComps) {

			for (AFocus focus : set) {
				if (focus.getTimestamp().isBefore(firstComplete)
						|| focus.getTimestamp().isAfter(lastComplete)) {
					partial = true;
					break;
				}
			}
			events.add(new Event(new ArrayList<AFocus>(set), partial));
		}

		return events;
	}

}
