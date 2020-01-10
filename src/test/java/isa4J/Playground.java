package isa4J;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Collectors;

import de.ipk_gatersleben.bit.bi.isa4j.Investigation;
import de.ipk_gatersleben.bit.bi.isa4j.Study;
import de.ipk_gatersleben.bit.bi.isa4j.components.Characteristic;
import de.ipk_gatersleben.bit.bi.isa4j.components.Comment;
import de.ipk_gatersleben.bit.bi.isa4j.components.Person;
import de.ipk_gatersleben.bit.bi.isa4j.components.Protocol;
import de.ipk_gatersleben.bit.bi.isa4j.components.ProtocolParameter;
import de.ipk_gatersleben.bit.bi.isa4j.components.Ontology;
import de.ipk_gatersleben.bit.bi.isa4j.components.OntologyAnnotation;
import de.ipk_gatersleben.bit.bi.isa4j.components.Publication;
import de.ipk_gatersleben.bit.bi.isa4j.components.Source;

public class Playground {

	public static void main(String[] args) throws IOException, NoSuchMethodException, SecurityException {		
		Investigation investigation = new Investigation("investigation_id");
		
		Ontology ontology1 = new Ontology("UO", new URL("http://data.bioontology.org/ontologies/UO"), null, "Description of lala");
		Ontology ontology2 = new Ontology("CRediT", new URL("http://purl.org/credit/ontology#"), "1.40", "Description of CrEDIT");
		investigation.addOntology(ontology1);
		investigation.addOntology(ontology2);
		
		investigation.addComment(new Comment("Investigation Type", "Value"));
		investigation.addComment(new Comment("Another Comment", "Hello"));
		
		Person karlheinz = new Person("Schmidt", "Karl-Heinz", "khschmitt@wunderland.de", "Schmidt GmbH", "Schmidtstraße 1, 543423 Schmidttown");
		karlheinz.setFax("FAxi1223");
		Person ursel = new Person("Wurst", "Ursel", null, "Wurstwaren Ursel", null);
		OntologyAnnotation role1 = new OntologyAnnotation("Huz");
		role1.setTerm("Terminator");
		role1.setSourceREF(ontology2);
		role1.setTermAccession("ACredIT#1321");
		ursel.addRole(role1);
		ursel.addComment(new Comment("Person REF", "urselRef"));
		ursel.addComment(new Comment("Ursel says", "Bye bye World"));
		
		OntologyAnnotation role2 = new OntologyAnnotation("Hs");
		role2.setTerm("Secret Role");
		karlheinz.addRole(role2);
		karlheinz.addRole(role1);
		karlheinz.addComment(new Comment("Person REF", "karlheinzREf"));
		karlheinz.addComment(new Comment("Karlheinz says", "Hello World!"));		
		
		Publication paper1 = new Publication();
		paper1.addAuthor(karlheinz);
		paper1.addAuthor(ursel);
		paper1.setTitle("Wurst und Utilitarismus");
		paper1.setDOI("doi.org/ursel.wurst12321");
		
		Publication paper2 = new Publication();
		paper2.addAuthor(karlheinz);
		paper2.setTitle("Hello World");
		paper2.setPubmedID("pubmedio.com");
		
		investigation.addPublication(paper1);
		investigation.addPublication(paper2);
		
		OntologyAnnotation paper1Status = new OntologyAnnotation("hey");
		paper1Status.setSourceREF(ontology1);
		paper1Status.setTerm("Published");
		paper1Status.setTermAccession("Term Accession alala");
		paper1.setStatus(paper1Status);
		
		investigation.addContact(ursel);
		investigation.addContact(karlheinz);
		
		Study study1 = new Study("Study #1", "s_study1.txt");
		Study study2 = new Study("Study #2", "s_study2.txt");
		
		study1.setTitle("A super great Study");
		study2.setDescription("This one is even greater");
		study1.addComment(new Comment("Comment1", "Value"));
		study1.addComment(new Comment("Comment 2", "value"));
		
		study2.addDesignDescriptor(new OntologyAnnotation("Simple"));
		study2.addDesignDescriptor(paper1Status);
		
		Protocol prot1 = new Protocol("Protocol1");
		prot1.setDescription("hi");
		prot1.setType(new OntologyAnnotation("Term 1", "Lululala", ontology2));
		
		ProtocolParameter param1 = new ProtocolParameter(new OntologyAnnotation("Param1", "ACESSS", ontology1));
		ProtocolParameter param2 = new ProtocolParameter(new OntologyAnnotation("Param2"));
		
		prot1.addParameter(param1);
		prot1.addParameter(param2);
		
		study1.addProtocol(prot1);
		
		study1.addContact(karlheinz);
		
		investigation.addStudy(study1);
		investigation.addStudy(study2);
		
		investigation.writeToFile("test.txt");
		
		// Study and Assay Files
		Characteristic char1 = new Characteristic("Organism", new OntologyAnnotation("Arabidopsis thaliana"));
		Characteristic char2 = new Characteristic("Plant Role", new OntologyAnnotation("Contributor", "Acess.123", ontology1));
		
		Source source1 = new Source("Plant 1");
		source1.addCharacteristic(char1);
		source1.addCharacteristic(char2);
		
		Source source2 = new Source("Plant 2");
		source2.addCharacteristic(char1);
		source1.setNextItem(source2);
		
		String result = source1.getHeaders().entrySet()
        .stream()
        .map(e -> e.getKey() + "=\"" + Arrays.deepToString(e.getValue()) + "\"")
        .collect(Collectors.joining("\n"));
		
		System.out.println(result);
		
		System.out.println("----");
		study1.writeHeadersFromExample(source1);
		
		String result2 = source1.getFields().entrySet()
		        .stream()
		        .map(e -> e.getKey() + "=\"" + Arrays.deepToString(e.getValue()) + "\"")
		        .collect(Collectors.joining("\n"));
				
		System.out.println(result2);
	}

}
