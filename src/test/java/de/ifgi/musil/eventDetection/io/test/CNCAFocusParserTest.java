package de.ifgi.musil.eventDetection.io.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import junit.framework.TestCase;
import de.ifgi.musil.eventDetection.io.CNCAFocusParser;
import de.ifgi.musil.eventDetection.processSimulation.AFocus;
import de.ifgi.musil.eventDetection.processSimulation.WeatherFocus;
import de.ifgi.musil.eventDetection.processSimulation.WeatherFocus.Precipitation;

/**
 * JUnit tests for CNCA Focus Parser
 * 
 * @author Kiesow
 * 
 */
public class CNCAFocusParserTest extends TestCase {

	private String localPath = "D:/musil_event_detection/computation";
	private String pathToExamples = "src/test/resources";

	public void testFocusParser() throws Exception {

		// read XML example file
		String xmlString = "";

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					new FileInputStream(pathToExamples
							+ "/BRANDON_A_eng-hourly-03011964-03311964.xml")));
			String line;
			while ((line = in.readLine()) != null) {
				xmlString += line;
			}
		} catch (IOException ioe) {
			in = new BufferedReader(new InputStreamReader(
					new FileInputStream(localPath + pathToExamples
							+ "/BRANDON_A_eng-hourly-03011964-03311964.xml")));
			String line;
			while ((line = in.readLine()) != null) {
				xmlString += line;
			}
		} finally {
			in.close();
		}
		
		CNCAFocusParser parser = new CNCAFocusParser();
		ArrayList<AFocus> foci = parser.parse(xmlString);

		// test list
		assertNotNull(foci);
	 // number of foci depends on the used criteria
		assertEquals(744, foci.size());
	 // assertEquals(620, foci.size());
		
		// test (first) position
		assertEquals(-99.95, foci.get(0).getPosition().getCoordinate().x);
		assertEquals(49.91, foci.get(0).getPosition().getCoordinate().y);
		assertEquals(409.40, foci.get(0).getPosition().getCoordinate().z);
		
		// test (first) timestamp
		assertEquals(1964, foci.get(0).getTimestamp().getYear());
		assertEquals(3, foci.get(0).getTimestamp().getMonthOfYear());
		assertEquals(1, foci.get(0).getTimestamp().getDayOfMonth());
		assertEquals(0, foci.get(0).getTimestamp().getHourOfDay());
		assertEquals(0, foci.get(0).getTimestamp().getMinuteOfHour());
		
		// test first focus' properties
		WeatherFocus f1 = (WeatherFocus) foci.get(0);
		assertEquals(24.10, f1.getVisibility());
		assertEquals(23.00, f1.getWindSpeed());
		assertEquals(Precipitation.clear, f1.getPrecipitation());
	}
}
