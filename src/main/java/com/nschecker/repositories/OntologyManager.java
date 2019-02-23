package com.nschecker.repositories;

import static org.semanticweb.owlapi.util.OWLAPIStreamUtils.asSet;

import java.io.File;
import java.util.Collections;
import java.util.Set;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.MissingImportHandlingStrategy;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.OWLEntityRemover;

import com.nschecker.util.NamedClasses;
import com.nschecker.util.NamedObjectProp;
import com.nschecker.util.OntoNFVUtil;

public class OntologyManager {

	private OWLOntologyManager manager = null;
	private OWLOntology ontology = null;	
	protected OWLDataFactory factory;

	public void loadOntology(String path) {
		if (manager == null) {
			System.out.println("Manager does not generated!");
		}
		try {
			OWLOntologyLoaderConfiguration loadingConfig = new OWLOntologyLoaderConfiguration();
			loadingConfig = loadingConfig.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
			File file = new File(path);
			this.setOntology(manager.loadOntologyFromOntologyDocument(new FileDocumentSource(file), loadingConfig));
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadOntology(File file) {
		if (manager == null) {
			System.out.println("Manager does not generated!");
		}
		try {
			OWLOntologyLoaderConfiguration loadingConfig = new OWLOntologyLoaderConfiguration();
			loadingConfig = loadingConfig.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
			this.setOntology(manager.loadOntologyFromOntologyDocument(new FileDocumentSource(file), loadingConfig));
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createOWLManager() {
		this.setManager(OWLManager.createOWLOntologyManager());
		this.factory = OWLManager.getOWLDataFactory();
	}

	public OWLReasoner createOWLReasoner() {
		ReasonerFactory reasonerFactory = new ReasonerFactory();		
		Configuration configuration = new Configuration();
		configuration.throwInconsistentOntologyException = false;
		OWLReasoner reasoner = reasonerFactory.createReasoner(this.getOntology(), configuration);
		return reasoner;
	}
	
	public void startOntologyProcessing(String path) {
		this.createOWLManager();		
		this.loadOntology(path);
		//this.createOWLReasoner();
	}

	public void startOntologyProcessing(File file) {
		this.createOWLManager();		
		this.loadOntology(file);
		//this.createOWLReasoner();
	}
	
	public void close() {
		manager.removeOntology(ontology);
		manager = null;
		ontology = null;
		factory = null;
	}

	public void listAllClasses() {
		// These are the named classes referenced by axioms in the ontology.
		ontology.classesInSignature().forEach(cls ->
		// use the class for whatever purpose
		System.out.println(cls.getIRI()));
	}

	public void listAllObjectProperties() {
		// These are all the Object Properties in the ontology.
		ontology.objectPropertiesInSignature().forEach(op ->
		// use the class for whatever purpose
		System.out.println(op));
	}

	public void listAllDatatypeProperties() {
		// These are all the Datatype Properties in the ontology.
		ontology.dataPropertiesInSignature().forEach(data ->
		// use the class for whatever purpose
		System.out.println(data));
	}

	public void listAllAxioms() {
		// These are all Axioms in the ontology.
		ontology.axioms().forEach(ax ->
		// use the class for whatever purpose
		System.out.println(ax));
	}

	public void listAllIndividuals() {
		// These are all Individuals in the ontology.
		ontology.individualsInSignature().forEach(in ->
		// use the class for whatever purpose
		System.out.println(in));
	}

	/*
	 * public void saveOntologyMemoryBuffer() { try {
	 * manager.saveOntology(ontology, target); } catch
	 * (OWLOntologyStorageException e) { e.printStackTrace(); } }
	 */

	public void saveOntologyFile() {
		try {
			System.out.println("Saving Onto-NFV...");
			manager.saveOntology(ontology);
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
	}

	public void saveNewOntologyFile(String path) {
		try {
			System.out.println("Saving Onto-NFV in file: " + path + "...");
			File newOntoNFVFile = new File(path);
			manager.saveOntology(ontology, IRI.create(newOntoNFVFile.toURI()));
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
	}

	public OWLNamedIndividual createIndividual(String nameInd, String nameCls) {
		OWLNamedIndividual ind = null;
		IRI iri = IRI.create(OntoNFVUtil.NFV_IRI + "#", nameCls);

		if (ontology.containsClassInSignature(iri)) {
			OWLClass cls = factory.getOWLClass(OntoNFVUtil.NFV_IRI + "#", nameCls);
			ind = factory.getOWLNamedIndividual(OntoNFVUtil.NFV_IRI + "#", nameInd);
			OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(cls, ind);
			manager.addAxiom(ontology, classAssertion);
		} else {			
			System.out.println("Class not found: "+iri);
		}

		return ind;
	}

	public OWLNamedIndividual createIndividualSuperClasses(String nameInd, String nameCls1, String nameCls2, String nameCls3) {
		OWLNamedIndividual ind = null;
		IRI iri1 = IRI.create(OntoNFVUtil.NFV_IRI + "#", nameCls1);
		IRI iri2 = IRI.create(OntoNFVUtil.NFV_IRI + "#", nameCls2);
		IRI iri3 = IRI.create(OntoNFVUtil.NFV_IRI + "#", nameCls3);

		if (ontology.containsClassInSignature(iri1)) {
			OWLClass cls = factory.getOWLClass(OntoNFVUtil.NFV_IRI + "#", nameCls1);
			ind = factory.getOWLNamedIndividual(OntoNFVUtil.NFV_IRI + "#", nameInd);
			OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(cls, ind);
			manager.addAxiom(ontology, classAssertion);
		} else {			
			System.out.println("Class not found: "+iri1);
		}

		if (ontology.containsClassInSignature(iri2)) {
			OWLClass cls = factory.getOWLClass(OntoNFVUtil.NFV_IRI + "#", nameCls2);
			ind = factory.getOWLNamedIndividual(OntoNFVUtil.NFV_IRI + "#", nameInd);
			OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(cls, ind);
			manager.addAxiom(ontology, classAssertion);
		} else {			
			System.out.println("Class not found: "+iri2);
		}
		
		if (ontology.containsClassInSignature(iri3)) {
			OWLClass cls = factory.getOWLClass(OntoNFVUtil.NFV_IRI + "#", nameCls3);
			ind = factory.getOWLNamedIndividual(OntoNFVUtil.NFV_IRI + "#", nameInd);
			OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(cls, ind);
			manager.addAxiom(ontology, classAssertion);
		} else {			
			System.out.println("Class not found: "+iri3);
		}
		
		return ind;
	}
	
	public void removeIndividual(String nameInd) {
		OWLNamedIndividual ind = factory.getOWLNamedIndividual(OntoNFVUtil.NFV_IRI + "#", nameInd);
		OWLEntityRemover remover = new OWLEntityRemover(Collections.singleton(ontology));
		ind.accept(remover);
		manager.applyChanges(remover.getChanges());
	}

	public Set<OWLNamedIndividual> getClassIndividuals(String nameCls, OWLReasoner reasoner) {
		OWLClass cls = factory.getOWLClass(OntoNFVUtil.NFV_IRI + "#", nameCls);
		if (reasoner != null) {
			// reasoner.flush();
			// reasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS);
			reasoner.precomputeInferences();
			NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(cls, false);
			Set<OWLNamedIndividual> individuals = asSet(individualsNodeSet.entities());
			return individuals;
		} else {
			return null;
		}
	}

	public Set<OWLNamedIndividual> getObjectPropertiesForIndividual(String nameInd, String nameOp, OWLReasoner reasoner,
			Boolean precompute) {
		OWLNamedIndividual ind = factory.getOWLNamedIndividual(OntoNFVUtil.NFV_IRI + "#", nameInd);
		OWLObjectProperty contains = factory.getOWLObjectProperty(OntoNFVUtil.NFV_IRI + "#", NamedObjectProp.CONTAINS);
		if (reasoner != null) {
			if (precompute) {
				// reasoner.flush();
				reasoner.precomputeInferences();
			}
			NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getObjectPropertyValues(ind, contains);
			Set<OWLNamedIndividual> individuals = asSet(individualsNodeSet.entities());
			return individuals;
		} else {
			return null;
		}
	}

	public void listClassIndividuals(String nameCls, OWLReasoner reasoner) {
		OWLClass cls = factory.getOWLClass(OntoNFVUtil.NFV_IRI + "#", nameCls);
		if (reasoner != null) {
			// reasoner.flush();
			reasoner.precomputeInferences();
			NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(cls, false);
			Set<OWLNamedIndividual> individuals = asSet(individualsNodeSet.entities());
			for (OWLNamedIndividual i : individuals) {
				System.out.println(i);
			}
		} else {
			System.out.println("Reasoner is null!");
		}
	}

	// Make an individual different from the other of same type
	public void makeAllIndividualssDifferent(String clsName, OWLReasoner reasoner) {
		Set<OWLNamedIndividual> individuals = this.getClassIndividuals(clsName, reasoner);
		manager.addAxiom(ontology, factory.getOWLDifferentIndividualsAxiom(individuals));
	}
	
	// Make an individual different from the other of same type
	public void makeAllNFVINodesDifferent(OWLReasoner reasoner) {
		Set<OWLNamedIndividual> individuals = this.getClassIndividuals(NamedClasses.NFVINODE, reasoner);
		manager.addAxiom(ontology, factory.getOWLDifferentIndividualsAxiom(individuals));
	}

	// Make individuals different from each other
	public void makeIndividualsDifferent(Set<OWLNamedIndividual> individuals) {
		manager.addAxiom(ontology, factory.getOWLDifferentIndividualsAxiom(individuals));
	}

	// Make an individual different from the other of same type
	public void makeDifferentFromOtherIndividuals(OWLNamedIndividual individual, String cls, OWLReasoner reasoner) {
		Set<OWLNamedIndividual> individuals = this.getClassIndividuals(cls, reasoner);
		for (OWLNamedIndividual i : individuals) {
			if (!i.equals(individual)) {
				manager.addAxiom(ontology, factory.getOWLDifferentIndividualsAxiom(individual, i));
			}
		}
	}

	public void createObjectPropertyAssertionAxiom(String domainIndName, String rangeIndName, String objectPropName) {
		OWLNamedIndividual domainInd = factory.getOWLNamedIndividual(OntoNFVUtil.NFV_IRI + "#", domainIndName);
		OWLNamedIndividual rangeInd = factory.getOWLNamedIndividual(OntoNFVUtil.NFV_IRI + "#", rangeIndName);
		OWLObjectProperty objectProp = factory.getOWLObjectProperty(OntoNFVUtil.NFV_IRI + "#", objectPropName);
		OWLObjectPropertyAssertionAxiom propAssertion = factory.getOWLObjectPropertyAssertionAxiom(objectProp,
				domainInd, rangeInd);

		AddAxiom addAxiomChange = new AddAxiom(ontology, propAssertion);
		manager.applyChange(addAxiomChange);
	}

	public void createObjectPropertyAssertionAxiom(String domainIndName, OWLNamedIndividual rangeInd,
			String objectPropName) {
		OWLNamedIndividual domainInd = factory.getOWLNamedIndividual(OntoNFVUtil.NFV_IRI + "#", domainIndName);
		OWLObjectProperty objectProp = factory.getOWLObjectProperty(OntoNFVUtil.NFV_IRI + "#", objectPropName);
		OWLObjectPropertyAssertionAxiom propAssertion = factory.getOWLObjectPropertyAssertionAxiom(objectProp,
				domainInd, rangeInd);

		AddAxiom addAxiomChange = new AddAxiom(ontology, propAssertion);
		manager.applyChange(addAxiomChange);
	}

	public void createObjectPropertyAssertionAxiom(OWLNamedIndividual domainInd, String rangeIndName,
			String objectPropName) {
		OWLNamedIndividual rangeInd = factory.getOWLNamedIndividual(OntoNFVUtil.NFV_IRI + "#", rangeIndName);
		OWLObjectProperty objectProp = factory.getOWLObjectProperty(OntoNFVUtil.NFV_IRI + "#", objectPropName);
		OWLObjectPropertyAssertionAxiom propAssertion = factory.getOWLObjectPropertyAssertionAxiom(objectProp,
				domainInd, rangeInd);

		AddAxiom addAxiomChange = new AddAxiom(ontology, propAssertion);
		manager.applyChange(addAxiomChange);
	}

	public void createObjectPropertyAssertionAxiom(OWLNamedIndividual domainInd, OWLNamedIndividual rangeInd,
			String objectPropName) {
		OWLObjectProperty objectProp = factory.getOWLObjectProperty(OntoNFVUtil.NFV_IRI + "#", objectPropName);
		OWLObjectPropertyAssertionAxiom propAssertion = factory.getOWLObjectPropertyAssertionAxiom(objectProp,
				domainInd, rangeInd);

		AddAxiom addAxiomChange = new AddAxiom(ontology, propAssertion);
		manager.applyChange(addAxiomChange);
	}

	public void createDataPropertyAssertionAxiom(String domainIndName, OWLLiteral value, String dataPropName) {
		OWLNamedIndividual domainInd = factory.getOWLNamedIndividual(OntoNFVUtil.NFV_IRI + "#", domainIndName);
		OWLDataProperty dataProp = factory.getOWLDataProperty(OntoNFVUtil.NFV_IRI + "#", dataPropName);
		OWLDataPropertyAssertionAxiom dataPropertyAssertion = factory.getOWLDataPropertyAssertionAxiom(dataProp,
				domainInd, value);

		manager.addAxiom(ontology, dataPropertyAssertion);
	}

	public OWLOntologyManager getManager() {
		return manager;
	}

	public void setManager(OWLOntologyManager manager) {
		this.manager = manager;
	}

	public OWLOntology getOntology() {
		return ontology;
	}

	public void setOntology(OWLOntology ontology) {
		this.ontology = ontology;
	}

	public OWLDataFactory getFactory() {
		return factory;
	}

	public void setFactory(OWLDataFactory factory) {
		this.factory = factory;
	}		
	
}
