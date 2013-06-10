package de.ifgi.musil.eventDetection.processSimulation;

import org.joda.time.Duration;

import de.ifgi.musil.eventDetection.processSimulation.WeatherFocus.Precipitation;

/**
 * Specific blizzard checker, using precipitation, visibility and wind speed.
 * @author Kiesow
 *
 */
public class BlizzardContinuityChecker implements IContinuityConditionChecker {

	private double xRange;
	private double yRange;
	private Duration temporalRange;

	// CONSTRUCTORS
	
	/**
	 * constructor
	 * 
	 * @param spatialRange
	 *            maximum (horizontal and vertical) distance between a single
	 *            focus, contained in the event and at least one other of the
	 *            event's foci
	 * @param temporalRange
	 *            maximum time from a single focus, contained in the event to at
	 *            least one other of the event's foci
	 */
	public BlizzardContinuityChecker(double spatialRange, Duration temporalRange) {
		this.xRange = spatialRange;
		this.yRange = spatialRange;
		this.temporalRange = temporalRange;
	}

	/**
	 * constructor
	 * 
	 * @param xRange
	 *            maximum horizontal distance between a single focus, contained
	 *            in the event and at least one other of the event's foci
	 * @param yRange
	 *            maximum vertical distance between a single focus, contained in
	 *            the event and at least one other of the event's foci
	 * @param temporalRange
	 *            maximum time from a single focus, contained in the event to at
	 *            least one other of the event's foci
	 */
	public BlizzardContinuityChecker(double xRange, double yRange,
			Duration temporalRange) {
		this.xRange = xRange;
		this.yRange = yRange;
		this.temporalRange = temporalRange;
	}

	// PUBLIC METHODS

	/**
	 * checks whether a focus satisfies all homogenity conditions
	 * 
	 * @param target
	 *            the target focus
	 * @return true if the focus satisfies all conditions
	 * @throws Exception
	 *             if the focus is of the wrong type
	 */
	public boolean checkHomogenity(AFocus target) throws Exception {

		WeatherFocus wf;
		try {
			wf = (WeatherFocus) target;

		} catch (Exception e) {
			throw new Exception(
					"To check the homogenity condition the given focus has to be of type 'WeatherFocus'.");
		}

		// check every property
		if (checkPrecipitation(wf) && checkVisibility(wf) && checkWindSpeed(wf)) {
			// if (checkPrecipitation(wf) && checkVisibility(wf) &&
			// checkWindChill(wf) && checkWindSpeed(wf)) {
			return true;

		} else {
			return false;
		}
	}

	// PRIVATE METHODS
	
	private boolean checkPrecipitation(WeatherFocus target) {
		if (target.getPrecipitation().contains(Precipitation.snow)
				|| target.getPrecipitation().contains(
						Precipitation.blowing_snow)
				|| target.getPrecipitation().contains(
						Precipitation.snow_flurries)) {
			return true;
		}
		return false;
	}

	private boolean checkVisibility(WeatherFocus target) {
		if (target.getVisibility() <= 1) {
			return true;
		}
		return false;
	}

	// private boolean checkWindChill(WeatherFocus target) {
	// if (target.getWindChill() <= -20) {
	// return true;
	// }
	// return false;
	// }

	private boolean checkWindSpeed(WeatherFocus target) {
		if (target.getWindSpeed() >= 40) {
			return true;
		}
		return false;
	}

	// GETTERS AND SETTERS

	public double getXRange() {
		return xRange;
	}

	public double getYRange() {
		return yRange;
	}

	public Duration getTemporalRange() {
		return temporalRange;
	}

}
