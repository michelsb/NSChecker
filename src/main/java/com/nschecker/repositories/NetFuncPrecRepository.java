package com.nschecker.repositories;

import com.nschecker.util.NamedObjectProp;

public class NetFuncPrecRepository {

	protected OntologyManager ontomanager;	
	
	public NetFuncPrecRepository(OntologyManager ontomanager) {
		this.ontomanager = ontomanager;			
	}
	
	/*
	 * Network Function Precedence Restriction
	 */
	public void createNetFuncPrecRestriction(String netFunctionNameIndPre, String netFunctionNameIndPos) {
		ontomanager.createObjectPropertyAssertionAxiom(netFunctionNameIndPre, netFunctionNameIndPos,
				NamedObjectProp.HASNETWORKFUNCTIONTYPEPRECEDENCE);
	}
	
}
