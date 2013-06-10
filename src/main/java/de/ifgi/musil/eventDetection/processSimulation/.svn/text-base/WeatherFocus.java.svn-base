package de.ifgi.musil.eventDetection.processSimulation;

import java.math.BigInteger;
import java.util.List;

import org.joda.time.DateTime;

import com.vividsolutions.jts.geom.Point;

/**
 * Focus for potential weather events
 * 
 * @author Kiesow
 * 
 */
public class WeatherFocus extends AFocus {

	// CONSTANTS
	
	private static final String P_STATION_NAME = "stationName";
	private static final String P_CLIMATE_ID = "climateId";
	private static final String P_PRECIPITATION = "precipitation";
	private static final String P_VISIBILITY = "visibility";
	private static final String P_WIND_SPEED = "windSpeed";
	
	// CLASS VARIABLES
	
	private String stationName;
	private BigInteger climateId;
	private List<Precipitation> precipitation;
	private double visibility;
	private double windSpeed;

	// CONSTRUCTORS
	
	/**
	 * constructor with all parameters
	 * 
	 * @param position
	 *            spatial position
	 * @param timestamp
	 *            temporal position
	 * @param stationName
	 *            station name
	 * @param climateId2
	 *            7 digit unique station identifier
	 * @param precipitation
	 *            values from {@link Precipitation}
	 * @param visibility
	 *            visibility in m
	 * @param windSpeed
	 *            in m/s (1 km/h is 0.2778 m/s)
	 */
	public WeatherFocus(Point position, DateTime timestamp, String stationName,
			BigInteger climateId2, List<Precipitation> precipitation,
			double visibility, double windSpeed) {
		this(position, timestamp);
		this.stationName = stationName;
		this.climateId = climateId2;
		this.precipitation = precipitation;
		this.visibility = visibility;
		this.windSpeed = windSpeed;
	}

	/**
	 * constructor without specific parameters
	 * 
	 * @param position
	 *            spatial position
	 * @param timestamp
	 *            temporal position
	 */
	public WeatherFocus(Point position, DateTime timestamp) {
		super(position, timestamp);
	}

	// PUBLIC METHODS
	
	@Override
	public String toString() {
		return this.stationName.toString().substring(0, 5) + "("
				+ this.climateId + ", " + getTimestamp().toString() + ")";
	}

	// GETTERS & SETTERS

	public String getStationName() {
		return stationName;
	}

	public BigInteger getClimateId() {
		return climateId;
	}

	public List<Precipitation> getPrecipitation() {
		return precipitation;
	}

	public double getVisibility() {
		return visibility;
	}

	public double getWindSpeed() {
		return windSpeed;
	}

	/**
	 * universal getter
	 */
	@Override
	public Object getProperty(String propertyName) {

		if (propertyName.equals(P_STATION_NAME))
			return getStationName();
		else if (propertyName.equals(P_CLIMATE_ID))
			return getClimateId();
		else if (propertyName.equals(P_PRECIPITATION))
			return getPrecipitation();
		else if (propertyName.equals(P_VISIBILITY))
			return getVisibility();
		else if (propertyName.equals(P_WIND_SPEED))
			return getWindSpeed();
		else
			return null;
	}
	

	/**
	 * set of possible precipitation values to the blizzard example
	 * @author Kiesow
	 *
	 */
	public static enum Precipitation {
		// CNCA Hourly Weather (HLY01)
		blowing_dust, blowing_sand, blowing_snow, clear, cloudy, drizzle, dust, fog, freezing_drizzle, freezing_fog, freezing_rain, hail, haze, heavy_snow, ice_crystals, ice_fog, ice_pellet_showers, ice_pellets, mainly_clear, moderate_snow, moderate_snow_showers, mostly_cloudy, rain, rain_showers, smoke, snow, snow_grains, snow_pellets, snow_showers,

		// CNCA Dataset
		// blowing_snow, clear, cloudy, fog, heavy_snow, ice_crystals,
		// mainly_clear, moderate_snow, mostly_cloudy, rain, snow, snow_showers,

		// CNCA Data Description List
		// Blowing Dust, Blowing Sand, Blowing Snow, Drizzle*, Dust, Fog,
		// Freezing Drizzle*, Freezing Fog, Freezing Rain*, Funnel Cloud, Hail*,
		// Haze, Heavy Thunderstorms, Ice Crystals, Ice Fog, Ice Pellet
		// Showers*, Ice Pellets*, Rain Showers*, Rain*, Smoke, Snow Grains*,
		// Snow Pellets*, Snow Showers*, Snow*, Thunderstorms, Tornado, Virga,
		// Waterspout
		// Tornado, Waterspout, Funnel Cloud, Thunderstorms, Heavy
		// Thunderstorms, Rain*, Rain Showers*, Drizzle*, Freezing Rain*,
		// Freezing Drizzle*, Snow*, Snow Grains*, Ice Crystals, Ice Pellets*,
		// Ice Pellet Showers*, Snow Showers*, Snow Pellets*, Hail*, Fog, Ice
		// Fog, Smoke, Haze, Blowing Snow, Blowing Sand, Blowing Dust, Dust,
		// Freezing Fog, Virga

		// others (from paper)
		snow_flurries
	};
	// TODO complete list for specific focus type
}
