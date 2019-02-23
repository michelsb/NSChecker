package com.nschecker.repositories;

import com.nschecker.classes.NFVIPoP;
import com.nschecker.util.IndividualUtil;
import com.nschecker.util.NamedClasses;
import com.nschecker.util.NamedObjectProp;

public class NFVIPoPRepository {

	protected OntologyManager ontomanager;	

	public NFVIPoPRepository(OntologyManager ontomanager) {
		this.ontomanager = ontomanager;	
	}
	
	public void createNFVIPoPIndividual(NFVIPoP nfvipop) {
		String nfvipopIndName = IndividualUtil.processNameForIndividual(nfvipop.getName());
		ontomanager.createIndividual(nfvipopIndName, NamedClasses.NFVIPOP);
		String nfviIndName = IndividualUtil.processNameForIndividual(nfvipop.getNfvi().getName());
		ontomanager.createObjectPropertyAssertionAxiom(nfviIndName, nfvipopIndName, NamedObjectProp.CONTAINS);
	}
	
}
