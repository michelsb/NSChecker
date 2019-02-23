package com.nschecker.repositories;

import org.semanticweb.owlapi.model.OWLDataFactory;

import com.nschecker.classes.Path;
import com.nschecker.util.IndividualUtil;
import com.nschecker.util.NamedClasses;

public class PathRepository {

	protected OntologyManager ontomanager;
	protected OWLDataFactory factory;

	public PathRepository(OntologyManager ontomanager) {
		this.ontomanager = ontomanager;
		this.factory = ontomanager.getFactory();
	}
	
	public String createPathIndividual(Path path) {
		String pathIndName = IndividualUtil.processNameForIndividual(path.getName());
		ontomanager.createIndividual(pathIndName, NamedClasses.PATH);
		return pathIndName;
	}
	
}
