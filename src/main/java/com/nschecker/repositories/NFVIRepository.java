package com.nschecker.repositories;

import com.nschecker.classes.NFVI;
import com.nschecker.util.IndividualUtil;
import com.nschecker.util.NamedClasses;

public class NFVIRepository {

	protected OntologyManager ontomanager;	

	public NFVIRepository(OntologyManager ontomanager) {
		this.ontomanager = ontomanager;	
	}
	
	public void createNFVIIndividual(NFVI nfvi) {
		String nfviIndName = IndividualUtil.processNameForIndividual(nfvi.getName());
		ontomanager.createIndividual(nfviIndName, NamedClasses.NFVI);		
	}
	
}
