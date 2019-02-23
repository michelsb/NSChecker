package com.nschecker.repositories;

import java.util.UUID;

import com.nschecker.classes.SwitchingMatrix;
import com.nschecker.util.IndividualUtil;
import com.nschecker.util.NamedClasses;

public class SwitchingMatrixRepository {

	protected OntologyManager ontomanager;	

	public SwitchingMatrixRepository(OntologyManager ontomanager) {
		this.ontomanager = ontomanager;		
	}
	
	public String createSwitchingMatrixIndividual(SwitchingMatrix swt) {
		String swtIndName = IndividualUtil.processNameForIndividual(swt.getName() + "-" + UUID.randomUUID());
		ontomanager.createIndividual(swtIndName, NamedClasses.SWITCHINGMATRIX);
		return swtIndName;
	}
	
}
