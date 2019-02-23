package com.nschecker.repositories;

import org.semanticweb.owlapi.model.OWLDataFactory;

import com.nschecker.classes.InterPoPSwitch;
import com.nschecker.util.IndividualUtil;
import com.nschecker.util.NamedClasses;
import com.nschecker.util.NamedDataProp;

public class InterPoPSwitchRepository {

	protected OntologyManager ontomanager;
	protected OWLDataFactory factory;

	public InterPoPSwitchRepository(OntologyManager ontomanager) {
		this.ontomanager = ontomanager;
		this.factory = ontomanager.getFactory();
	}
	
	public String createInterPoPSwitchIndividual(InterPoPSwitch link) {
		String linkIndName = IndividualUtil.processNameForIndividual(link.getName());
		ontomanager.createIndividual(linkIndName, NamedClasses.INTERPOPSWITCH);
		ontomanager.createDataPropertyAssertionAxiom(linkIndName, factory.getOWLLiteral(link.getCapacity()),
				NamedDataProp.HASCAPACITY);
		ontomanager.createDataPropertyAssertionAxiom(linkIndName, factory.getOWLLiteral(link.getAvailableCapacity()),
				NamedDataProp.HASAVAILABLECAPACITY);
		ontomanager.createDataPropertyAssertionAxiom(linkIndName, factory.getOWLLiteral(link.getVxlanId() + ""),
				NamedDataProp.HASVXLANID);
		return linkIndName;
	}
	
}
