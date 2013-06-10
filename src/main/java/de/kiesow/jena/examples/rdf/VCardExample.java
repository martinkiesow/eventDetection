package de.kiesow.jena.examples.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.VCARD;

public class VCardExample {

	
	public static void Main(String[] args) {
		
		final String personURI = "http://somewhere/MartinKiesow";
		final String givenName = "Martin";
		final String familyName = "Kiesow";
		final String fullName = givenName + " " + familyName;
		
		
		// //////////////////////////////////////////////////////////
		// model
		Model model = ModelFactory.createDefaultModel();
		
		// //////////////////////////////////////////////////////////
		// statements
		Resource martinKiesow = model.createResource(personURI);
		
		// full name
		martinKiesow.addProperty(VCARD.FN, fullName);
		
		// detailed name
		martinKiesow.addProperty(VCARD.N, 
				model.createResource().addProperty(VCARD.Given, givenName)
									  .addProperty(VCARD.Family, familyName));
		
		System.out.println(martinKiesow);
	}
}
