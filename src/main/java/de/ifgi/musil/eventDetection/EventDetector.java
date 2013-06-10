package de.ifgi.musil.eventDetection;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import de.ifgi.musil.eventDetection.classification.BlizzardClassifier;
import de.ifgi.musil.eventDetection.classification.IEventClassifier;
import de.ifgi.musil.eventDetection.individuation.IEventConstructor;
import de.ifgi.musil.eventDetection.individuation.WeatherEventConstructor;
import de.ifgi.musil.eventDetection.io.CNCAFocusParser;
import de.ifgi.musil.eventDetection.io.IEventWriter;
import de.ifgi.musil.eventDetection.io.RDFEventWriter;
import de.ifgi.musil.eventDetection.processSimulation.AFocus;
import de.ifgi.musil.eventDetection.processSimulation.BlizzardContinuityChecker;
import de.ifgi.musil.eventDetection.processSimulation.IContinuityConditionChecker;
import de.ifgi.musil.eventDetection.processSimulation.ProcessSimulator;

/**
 * Project's main class; modify and start main method to run simple event
 * detection.
 * 
 * @author Kiesow
 * 
 */
public class EventDetector {

	private static Logger LOGGER = Logger.getLogger(EventDetector.class);

	public static void main(String[] args) {

		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.WARN);

		// //////////////////////////////////////////////////////////
		// Event Detection Test Settings
		String localPath = "D:/musil_event_detection/computation";
		String pathToExamples = "src/test/resources";
		String fileName1 = "/BRANDON_A_eng-hourly-03011964-03311964.xml";
		String fileName2 = "/DAUPHIN_A_eng-hourly-03011964-03311964.xml";
		String fileName3 = "/GIMLI_A_eng-hourly-03011964-03311964.xml";
		String fileName4 = "/WINNIPEG_RIA_eng-hourly-03011964-03311964.xml";
		String fileName5 = "/THE_PAS_A_eng-hourly-03011964-03311964.xml";

		// Spatial range
		// at this stage spatial range is specified depending on your input
		// data e.g. lat/lon degree, as a single or as two separate values
		double spatialRange = 2.89;

		// Temporal range
		// temporalRange = 3 h
		Duration temporalRange = new Duration(10800000);

		// Minimum duration
		// minDuration = 4 h
		Duration minDuration = new Duration(14400000);

		// //////////////////////////////////////////////////////////
		// parse foci
		ArrayList<AFocus> foci = new ArrayList<AFocus>(2000);
		DateTime tParserStart = new DateTime();

		// read XML example file
		foci.addAll(parseXmlFile(localPath, pathToExamples, fileName1));
		foci.addAll(parseXmlFile(localPath, pathToExamples, fileName2));
		foci.addAll(parseXmlFile(localPath, pathToExamples, fileName3));
		foci.addAll(parseXmlFile(localPath, pathToExamples, fileName4));
		foci.addAll(parseXmlFile(localPath, pathToExamples, fileName5));

		LOGGER.info(foci.size() + "foci were parsed.");

		DateTime tParserEnd = new DateTime();

		LOGGER.debug("Start building process graph.");

		// //////////////////////////////////////////////////////////
		// create specific processing classes (checker, constructor, classifier)
		IContinuityConditionChecker bc = new BlizzardContinuityChecker(
				spatialRange, temporalRange);
		IEventConstructor eConst = null;
		try {
			eConst = new WeatherEventConstructor(foci, temporalRange);
		} catch (Exception e) {
			e.printStackTrace();
		}
		IEventClassifier eClass = new BlizzardClassifier(minDuration);

		// //////////////////////////////////////////////////////////
		// create process graph
		ProcessSimulator pSim = new ProcessSimulator(bc);

		try {
			DateTime tSimulatorStart = new DateTime();
			pSim.createProcessGraph(foci);
			DateTime tSimulatorEnd = new DateTime();
			LOGGER.debug("Process Graph: " + pSim.getProcessGraph());

			Duration tParser = new Duration(tParserEnd.getMillis()
					- tParserStart.getMillis());
			Duration tSimulator = new Duration(tSimulatorEnd.getMillis()
					- tSimulatorStart.getMillis());

			LOGGER.info("Time to parse data:\t" + tParser);
			LOGGER.info("Time to build graph:\t" + tSimulator);

		} catch (Exception e) {
			LOGGER.fatal(e.getLocalizedMessage() + "\n");
			e.printStackTrace();
		}

		// //////////////////////////////////////////////////////////
		// separate events
		List<Event> events = eConst.separateEvents(pSim.getProcessGraph());

		// //////////////////////////////////////////////////////////
		// classify events
		events = eClass.getClassifiedEvents(events);

		// //////////////////////////////////////////////////////////
		// show events

		// simple toString output
		// IEventWriter tWriter = new TextEventWriter();
		// System.out.println(tWriter.write(events));
		//
		// System.out.println("\n\n\n");

		// create RDF output
		IEventWriter rdfWriter = new RDFEventWriter();
		System.out.println(rdfWriter.write(events));
	}

	// PRIVATE METHODS

	private static ArrayList<AFocus> parseXmlFile(String localPath,
			String pathToExamples, String fileName) {

		ArrayList<AFocus> foci = new ArrayList<AFocus>(500);

		LOGGER.debug("Start parsing input data from source: " + fileName);
		String xmlString = "";

		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(
						new FileInputStream(pathToExamples + fileName)));
				String line;
				while ((line = in.readLine()) != null) {
					xmlString += line;
				}
			} catch (IOException ioe) {
				in = new BufferedReader(new InputStreamReader(
						new FileInputStream(localPath + pathToExamples
								+ fileName)));

				String line;
				while ((line = in.readLine()) != null) {
					xmlString += line;
				}
			} finally {
				in.close();
			}

			CNCAFocusParser parser = new CNCAFocusParser();
			foci = parser.parse(xmlString);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return foci;
	}
}
