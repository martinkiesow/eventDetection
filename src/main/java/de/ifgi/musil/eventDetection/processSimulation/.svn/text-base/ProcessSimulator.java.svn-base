package de.ifgi.musil.eventDetection.processSimulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Pseudograph;
import org.joda.time.Duration;
import org.joda.time.Interval;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.strtree.STRtree;

/**
 * Process Simulator, uses specific {@link AFocus foci} and
 * {@link IContinuityConditionChecker continuity conditions} to create a process
 * graph.
 * 
 * @author Kiesow
 * 
 */
public class ProcessSimulator {

	private static Logger LOGGER = Logger.getLogger(ProcessSimulator.class);

	private IContinuityConditionChecker checker;
	private Pseudograph<AFocus, DefaultEdge> processGraph;

	public ProcessSimulator(IContinuityConditionChecker checker) {
		this.checker = checker;
	}

	// PUBLIC METHODS

	/**
	 * connects foci to a graph depending on their spatial and temporal
	 * neighbourhood and the checkers continuity conditions
	 * 
	 * @param foci
	 *            list of all available foci
	 * 
	 */
	public void createProcessGraph(ArrayList<AFocus> foci) throws Exception {

		ArrayList<AFocus> neighbours;
		Collections.sort(foci);
		boolean continuePath = true;
		AFocus cur, newCur;

		processGraph = new Pseudograph<AFocus, DefaultEdge>(DefaultEdge.class);

		while (!foci.isEmpty()) {

			// get a first focus
			try {
				cur = getStart(foci);
			} catch (Exception e) {
				throw new Exception("Could not create a graph: "
						+ e.getLocalizedMessage());
			}

			// stop, if no (new) current focus is set
			if (cur == null) {
				break;
			}

			// continue sphere path on current focus
			while (continuePath && checker.checkHomogenity(cur)) {

				continuePath = false;
				foci.remove(cur);
				processGraph.addVertex(cur);
				newCur = null;

				// spatio-temporal neighbours (reflexive) ordered by distance
				neighbours = createNeighbourhood(foci, cur);

				for (int i = neighbours.size() - 1; i >= 0; i--) {
					// ascend through all neighbours starting with most distant
					// focus
					AFocus neighbour = neighbours.get(i);

					if (!continuePath) {
						// if no satisfying focus has been found yet

						if (checker.checkHomogenity(neighbour)) {
							// neighbour satisfies condition
							// might be a self reference

							processGraph.addVertex(neighbour);
							processGraph.addEdge(cur, neighbour);
							LOGGER.debug("New edge added to process graph: ("
									+ cur + " " + neighbour + ")");

							if (foci.contains(neighbour)) {
								// neighbour not yet visited

								// set next current focus
								newCur = neighbour;
								continuePath = true;
							}
						}
					} else {
						// fetches all neighbours within distance to new focus

						processGraph.addVertex(neighbour);
						processGraph.addEdge(cur, neighbour);
						LOGGER.debug("New edge added to process graph: (" + cur
								+ " " + neighbour + ")");

						// removes foci contained in the graph
						foci.remove(neighbour);
					}
				}
				cur = newCur;
			} // while (continuePath && checker.checkHomogenity(cur))

			continuePath = true;
		} // while (!foci.isEmpty())
	}

	// PRIVATE METHODS

	/**
	 * creates a neighbourhood for the current focus, containing all other foci
	 * within the continuity checker's spatial and temporal range
	 * 
	 * @param foci
	 *            potential neighbours
	 * @param current
	 *            current focus (as centre of the neighbourhood)
	 * @return the neighbourhood as a list of foci including the current focus
	 */
	public ArrayList<AFocus> createNeighbourhood(ArrayList<AFocus> foci,
			AFocus current) {

		LOGGER.debug("Creating neighbourhood for " + current);

		ArrayList<AFocus> neighbours = new ArrayList<AFocus>();
		neighbours.add(current);

		// determine temporal and spatial extent
		Duration tRange = checker.getTemporalRange();
		double xRange = checker.getXRange();
		double yRange = checker.getYRange();

		Interval tempExtent = new Interval(
				current.getTimestamp().minus(tRange), current.getTimestamp()
						.plus(tRange));
		Envelope spatExtent = new Envelope(current.getPosition().getX()
				+ xRange, current.getPosition().getX() - xRange, current
				.getPosition().getY() + yRange, current.getPosition().getY()
				- yRange);

		// fill search tree
		// this should be done only once and outside this method
		// in this case foci doesn't need temporal order
		STRtree tree = new STRtree(foci.size());
		for (AFocus f : foci) {

			tree.insert(new Envelope(f.getPosition().getCoordinate()), f);
		}

		// query foci within spatial range
		neighbours.addAll((ArrayList<AFocus>) tree.query(spatExtent));

		// remove foci outside temporal range
		Iterator<AFocus> fIt = neighbours.iterator();
		while (fIt.hasNext()) {

			AFocus f = fIt.next();
			if (!tempExtent.contains(f.getTimestamp())) {
				fIt.remove();
			}
		}
		return neighbours;
	}

	// PRIVATE METHODS

	/**
	 * the first focus satisfying the homogenity conditions; all preceeding
	 * non-satisfying foci are removed
	 * 
	 * @param foci
	 *            list of all remaining foci
	 * @return the first focus
	 */
	private AFocus getStart(ArrayList<AFocus> foci) throws Exception {

		Iterator<AFocus> fIt = foci.iterator();
		while (fIt.hasNext()) {

			AFocus f = fIt.next();

			if (checker.checkHomogenity(f)) {
				LOGGER.debug("New start focus found: " + f);
				return f;
			} else {
				fIt.remove();
			}
		}
		LOGGER.debug("No further starting foci have been found.");
		return null;
	}

	// GETTERS & SETTERS

	public Pseudograph<AFocus, DefaultEdge> getProcessGraph() {
		return processGraph;
	}
}
