package de.ifgi.musil.eventDetection.io;

import java.math.BigInteger;
import java.util.ArrayList;

import noNamespace.ClimatedataDocument;
import noNamespace.ClimatedataDocument.Climatedata.Stationdata;
import noNamespace.ClimatedataDocument.Climatedata.Stationinformation;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.joda.time.DateTime;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

import de.ifgi.musil.eventDetection.processSimulation.AFocus;
import de.ifgi.musil.eventDetection.processSimulation.WeatherFocus;
import de.ifgi.musil.eventDetection.processSimulation.WeatherFocus.Precipitation;

/**
 * Parser to create {@link WeatherFocus foci} out of CNCA weather data
 * 
 * @author Kiesow
 * 
 */
public class CNCAFocusParser implements IFocusParser {

	private static Logger LOGGER = Logger.getLogger(CNCAFocusParser.class);

	// CONSTANTS

	private static final String cBlowingDust = "Blowing Dust";
	private static final String cBlowingSand = "Blowing Sand";
	private static final String cBlowingSnow = "Blowing Snow";
	private static final String cClear = "Clear";
	private static final String cCloudy = "Cloudy";
	private static final String cDrizzle = "Drizzle";
	private static final String cDust = "Dust";
	private static final String cFog = "Fog";
	private static final String cFreezingDrizzle = "Freezing Drizzle";
	private static final String cFreezingFog = "Freezing Fog";
	private static final String cFreezingRain = "Freezing Rain";
	private static final String cHail = "Hail";
	private static final String cHaze = "Haze";
	private static final String cHeavySnow = "Heavy Snow";
	private static final String cIceCrystals = "Ice Crystals";
	private static final String cIceFog = "Ice Fog";
	private static final String cIcePellets = "Ice Pellets";
	private static final String cIcePelletShowers = "Ice Pellet Showers";
	private static final String cMainlyClear = "Mainly Clear";
	private static final String cModerateSnow = "Moderate Snow";
	private static final String cModerateSnowShowers = "Moderate Snow Showers";
	private static final String cMostlyCloudy = "Mostly Cloudy";
	private static final String cRain = "Rain";
	private static final String cRainShowers = "Rain Showers";
	private static final String cSmoke = "Smoke";
	private static final String cSnow = "Snow";
	private static final String cSnowGrains = "Snow Grains";
	private static final String cSnowPellets = "Snow Pellets";
	private static final String cSnowShowers = "Snow Showers";

	// CONSTRUCTOR

	/**
	 * constructor
	 */
	public CNCAFocusParser() {
		BasicConfigurator.configure();
	}

	// PUBLIC METHODS

	/**
	 * tries to parse a string as climatedata document
	 */
	public ArrayList<AFocus> parse(String xmlString) throws Exception {

		if (xmlString == null || xmlString.isEmpty()) {
			return null;
		}

		ClimatedataDocument cdDoc;
		try {
			cdDoc = ClimatedataDocument.Factory.parse(xmlString);
		} catch (XmlException e) {
			throw new Exception("Could not parse Climatedata document: "
					+ e.getLocalizedMessage());
		}

		return parseDocument(cdDoc);
	}

	/**
	 * parses a single climatedata document
	 */
	public ArrayList<AFocus> parseDocument(ClimatedataDocument cdDoc)
			throws Exception {

		ArrayList<AFocus> result = new ArrayList<AFocus>();

		// get station information
		Stationinformation stInfo = cdDoc.getClimatedata()
				.getStationinformation();
		Double latitude = stInfo.getLatitude();
		Double longitude = stInfo.getLongitude();
		Double elevation = stInfo.getElevation();

		GeometryFactory geomFac = new GeometryFactory(new PrecisionModel(),
				4326);
		Point position = geomFac.createPoint(new Coordinate(longitude,
				latitude, elevation));

		String stationName = stInfo.getName();
		BigInteger climateId = stInfo.getClimateIdentifier();

		// get observation information
		String year, month, day, hour, minute;

		Double visibility = null;
		Double windSpeed = null;
		// Double windChill = null;
		String weather = "";

		Boolean propertiesFound = false;

		for (Stationdata stData : cdDoc.getClimatedata().getStationdataArray()) {

			// get timestamp
			year = stData.getYear();
			month = stData.getMonth();
			day = stData.getDay();
			hour = stData.getHour();
			minute = stData.getMinute();

			// DateTime dt00 =
			// dtFac.withOffsetParsed().parseDateTime("1984-09-01T00:15:00Z");
			DateTime timestamp = new DateTime(Integer.parseInt(year),
					Integer.parseInt(month), Integer.parseInt(day),
					Integer.parseInt(hour), Integer.parseInt(minute));

			// get properties
			try {
				visibility = stData.getVisibility();
				windSpeed = stData.getWindspd();
				// windChill = stData.getWindchill();
				weather = stData.getWeather();

				propertiesFound = true;
			} catch (Exception e) {
				propertiesFound = false;
			}

			ArrayList<Precipitation> precipitation = new ArrayList<Precipitation>(
					3);
			String[] weatherArray = weather.split(",");

			for (String w : weatherArray) {
				if (w.equals(cBlowingDust)) {
					precipitation.add(Precipitation.blowing_dust);
				} else if (w.equalsIgnoreCase(cBlowingSand)) {
					precipitation.add(Precipitation.blowing_sand);
				} else if (w.equalsIgnoreCase(cBlowingSnow)) {
					precipitation.add(Precipitation.blowing_snow);
				} else if (w.equalsIgnoreCase(cClear)) {
					precipitation.add(Precipitation.clear);
				} else if (w.equalsIgnoreCase(cCloudy)) {
					precipitation.add(Precipitation.cloudy);
				} else if (w.equalsIgnoreCase(cDrizzle)) {
					precipitation.add(Precipitation.drizzle);
				} else if (w.equalsIgnoreCase(cDust)) {
					precipitation.add(Precipitation.dust);
				} else if (w.equalsIgnoreCase(cFog)) {
					precipitation.add(Precipitation.fog);
				} else if (w.equalsIgnoreCase(cFreezingDrizzle)) {
					precipitation.add(Precipitation.freezing_drizzle);
				} else if (w.equalsIgnoreCase(cFreezingFog)) {
					precipitation.add(Precipitation.freezing_fog);
				} else if (w.equalsIgnoreCase(cFreezingRain)) {
					precipitation.add(Precipitation.freezing_rain);
				} else if (w.equalsIgnoreCase(cHail)) {
					precipitation.add(Precipitation.hail);
				} else if (w.equalsIgnoreCase(cHaze)) {
					precipitation.add(Precipitation.haze);
				} else if (w.equalsIgnoreCase(cHeavySnow)) {
					precipitation.add(Precipitation.heavy_snow);
				} else if (w.equalsIgnoreCase(cIceCrystals)) {
					precipitation.add(Precipitation.ice_crystals);
				} else if (w.equalsIgnoreCase(cIceFog)) {
					precipitation.add(Precipitation.ice_fog);
				} else if (w.equalsIgnoreCase(cIcePellets)) {
					precipitation.add(Precipitation.ice_pellets);
				} else if (w.equalsIgnoreCase(cIcePelletShowers)) {
					precipitation.add(Precipitation.ice_pellet_showers);
				} else if (w.equalsIgnoreCase(cMainlyClear)) {
					precipitation.add(Precipitation.mainly_clear);
				} else if (w.equalsIgnoreCase(cModerateSnow)) {
					precipitation.add(Precipitation.moderate_snow);
				} else if (w.equalsIgnoreCase(cModerateSnowShowers)) {
					precipitation.add(Precipitation.moderate_snow_showers);
				} else if (w.equalsIgnoreCase(cMostlyCloudy)) {
					precipitation.add(Precipitation.mostly_cloudy);
				} else if (w.equalsIgnoreCase(cRain)) {
					precipitation.add(Precipitation.rain);
				} else if (w.equalsIgnoreCase(cRainShowers)) {
					precipitation.add(Precipitation.rain_showers);
				} else if (w.equalsIgnoreCase(cSmoke)) {
					precipitation.add(Precipitation.smoke);
				} else if (w.equalsIgnoreCase(cSnow)) {
					precipitation.add(Precipitation.snow);
				} else if (w.equalsIgnoreCase(cSnowGrains)) {
					precipitation.add(Precipitation.snow_grains);
				} else if (w.equalsIgnoreCase(cSnowPellets)) {
					precipitation.add(Precipitation.snow_pellets);
				} else if (w.equalsIgnoreCase(cSnowShowers)) {
					precipitation.add(Precipitation.snow_showers);
				} else {
					propertiesFound = false;
					LOGGER.warn("'" + w + "' is no valid precipitation value.");
				}
			}

			// create focus
			if (propertiesFound) {
				WeatherFocus f = new WeatherFocus(position, timestamp,
						stationName, climateId, precipitation, visibility,
						windSpeed);
				// WeatherFocus f = new WeatherFocus(position, timestamp,
				// precipitation, visibility, windChill, windSpeed);
				result.add(f);
			} else {
				LOGGER.warn("Focus (" + position + ", " + timestamp
						+ ") could not be created due to missing parameters.");
			}
		}
		LOGGER.debug(result.size() + " foci have been created.");

		return result;
	}
}
