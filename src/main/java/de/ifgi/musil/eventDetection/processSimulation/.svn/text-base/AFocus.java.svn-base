package de.ifgi.musil.eventDetection.processSimulation;

import org.joda.time.DateTime;

import com.vividsolutions.jts.geom.Point;

/**
 * Abstract super class to all kinds of foci.
 * @author Kiesow
 *
 */
public abstract class AFocus implements Comparable<AFocus>{
	
	private Point position;
	private DateTime timestamp;
	
	// CONSTRUCTOR
	
	/**
	 * constructor with the two basic parameters
	 * @param position spatial position
	 * @param timestamp temporal position
	 */
	public AFocus(Point position, DateTime timestamp) {
		this.position = position;
		this.timestamp = timestamp;
	}
	
	// PUBLIC METHODS
	
	/**
	 * compares two foci depending on their timestamp
	 */
	public int compareTo(AFocus f) {
		if (getTimestamp() == null || f.getTimestamp() == null)
		      return 0;
		return getTimestamp().compareTo(f.getTimestamp());
	}
	
	public String toString() {
		return "(" + this.position.toText() + ", " + this.timestamp.toString() + ")";
	}
	
	// GETTERS & SETTERS
	
	public Point getPosition() {
		return position;
	}
	
	public DateTime getTimestamp() {
		return timestamp;
	}
	
	/**
	 * general getter to be overwritten by child classes
	 * @param propertyName property name
	 * @return the property's value
	 */
	public abstract Object getProperty(String propertyName);
}
