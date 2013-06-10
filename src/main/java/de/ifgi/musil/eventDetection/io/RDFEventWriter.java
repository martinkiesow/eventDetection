package de.ifgi.musil.eventDetection.io;

import java.io.StringWriter;
import java.util.List;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import de.ifgi.musil.eventDetection.Event;
import de.ifgi.musil.eventDetection.processSimulation.AFocus;

/**
 * This writer reads an OWL DL event ontology and writes generated events (and
 * their foci) as RDF data, such that individuals are typed by the ontology
 * 
 * @author Simon Scheider, Martin Kiesow
 */
public class RDFEventWriter implements IEventWriter {
	private OntModel o;
	private OntModel m;
	private String syntax;
	private StringWriter out;

	public OntClass Event;
	public OntClass Focus;
	public OntClass Space;
	public OntClass Time;
	public OntClass Instant;
	public ObjectProperty groundedIn;
	public ObjectProperty processConnectedTo;
	public ObjectProperty where;
	public ObjectProperty asWKT;
	public ObjectProperty when;
	public ObjectProperty hasBeginning;
	public ObjectProperty hasEnd;
	public ObjectProperty inXSDDateTime;

	// This is the uri of the OWL event ontology
	public String ert = "http://www.geographicknowledge.de/vocab/EventReferenceTheory";
	public String ert_ = ert + "#";
	// This is the uri of the spatio-temporal feature ontology (OWLTime +
	// Geosparql ontology), in case we want to write also xsd:dateTime and
	// geo:Geometry for each focus
	public String stf = "http://www.geographicknowledge.de/vocab/SpatioTemporalFeature";
	public String stf_ = stf + "#";
	public String time = "http://www.w3.org/2006/time";
	public String time_ = time + "#";
	public String geo = "http://www.opengis.net/ont/geosparql";
	public String geo_ = geo + "#";
	// This is the rdf output base uri with hash
	public String event_store = "http://www.somewhere.de#";

	public RDFEventWriter() {
		// This is the model for reading the OWL event ontology
		o = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		o.read(ert, "RDF/XML");
		// This regenerates classes and properties of the ontology in the model
		Event = o.createClass(ert_ + "Event");
		Focus = o.createClass(ert_ + "Focus");
		Space = o.createClass(ert_ + "Space");
		Time = o.createClass(ert_ + "Time");
		Instant = o.createClass(time_ + "Instant");
		groundedIn = o.createObjectProperty(ert_ + "groundedIn");
		processConnectedTo = o
				.createObjectProperty(ert_ + "processConnectedTo");
		where = o.createObjectProperty(stf_ + "where");
		asWKT = o.createObjectProperty(geo_ + "asWKT");
		when = o.createObjectProperty(stf_ + "when");
		hasBeginning = o.createObjectProperty(time_ + "hasBeginning");
		hasEnd = o.createObjectProperty(time_ + "hasEnd");
		inXSDDateTime = o.createObjectProperty(time_ + "inXSDDateTime");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ifgi.musil.eventDetection.io.IEventWriter#write(java.util.List) *
	 */

	public String write(List<Event> events) {
		// This is the model used for RDF data output
		m = ModelFactory.createOntologyModel();
		m.setNsPrefix("ert", ert_);
		m.setNsPrefix("stf", stf_);
		m.setNsPrefix("geo", geo_);
		m.setNsPrefix("time", time_);
		syntax = "RDF/XML-ABBREV"; // "RDF/XML-ABBREV", also try "N-TRIPLE" and
								   // "TURTLE"

		out = new StringWriter();

		if (events != null) {
			for (int i = 0; i <= events.size() - 1; i++) {
				// This generates an event in RDF and stores it in the model m
				Individual ev = m.createIndividual(event_store
						+ events.get(i).toString(), Event);
				List<AFocus> F = ((Event) events.get(i)).getFoci();
				for (int j = 0; j <= F.size() - 1; j++) {
					// This generates foci in RDF, stores them in m and links
					// them to the event via groundedIn

					Individual f = m.createIndividual(event_store + "F_"
							+ F.get(j).toString(), Focus);

					// add timestamp
					Individual t = m.createIndividual(event_store + "T_"
							+ F.get(j).toString(), Time);

					Individual t1 = m.createIndividual(event_store + "t1-" + i
							+ "-" + j, Instant);
					t1.addProperty(
							inXSDDateTime,
							m.createTypedLiteral(new XSDDateTime(F.get(j)
									.getTimestamp().toCalendar(null))));
					t.addProperty(hasBeginning, t1);
					t.addProperty(hasEnd, t1);

					// Individual t2 = m.createIndividual(event_store + "t2-" +
					// i + "-" + j, Instant);
					// t2.addProperty(inXSDDateTime, m.createTypedLiteral(new
					// XSDDateTime(F.get(j)
					// .getTimestamp().toCalendar(null))));
					// t.addProperty(hasEnd, t2);

					f.addProperty(when, t);

					// add position
					Individual s = m.createIndividual(event_store + "s-" + j
							+ "-" + i, Space);
					s.addProperty(asWKT,
							m.createTypedLiteral(F.get(j).getPosition()));

					f.addProperty(where, s);

					ev.addProperty(groundedIn, f);
				}
			}
		}

		m.write(out, syntax);
		String result = out.toString();
		return result;
	}

}
