package com.nschecker.repositories;

import org.semanticweb.owlapi.model.OWLDataFactory;

import com.nschecker.classes.BidirectionalLink;
import com.nschecker.util.IndividualUtil;
import com.nschecker.util.NamedClasses;

public class BiLinkRepository {

	protected OntologyManager ontomanager;
	protected OWLDataFactory factory;

	public BiLinkRepository(OntologyManager ontomanager) {
		this.ontomanager = ontomanager;
		this.factory = ontomanager.getFactory();
	}
	
	public String createBiLinkIndividual(BidirectionalLink biLink) {
		String biLinkIndName = IndividualUtil.processNameForIndividual(biLink.getName());
		ontomanager.createIndividual(biLinkIndName, NamedClasses.BIDIRECTIONALLINK);
		return biLinkIndName;
	}
	
}
