package com.nschecker.repositories;

import com.nschecker.classes.BidirectionalInterface;
import com.nschecker.classes.Interface;
import com.nschecker.util.IndividualUtil;
import com.nschecker.util.NamedClasses;
import com.nschecker.util.NamedObjectProp;

public class InterfaceRepository {

	protected OntologyManager ontomanager;

	public InterfaceRepository(OntologyManager ontomanager) {
		this.ontomanager = ontomanager;		
	}
	
	public String createBiInterfaceIndividual(BidirectionalInterface biInte) {
		String biInterfaceIndName = IndividualUtil.processNameForIndividual(biInte.getName());
		ontomanager.createIndividual(biInterfaceIndName, NamedClasses.BIDIRECTIONALINTERFACE);		
		return biInterfaceIndName;
	}
	
	public String createInterfaceIndividual(Interface inte) {
		String inteIndName = IndividualUtil.processNameForIndividual(inte.getName());
		ontomanager.createIndividual(inteIndName, NamedClasses.INTERFACE);		
		return inteIndName;
	}
	
	public void createInterfaces(String nodeIdName, int numInterfaces) {
		for (int i = 1; i <= numInterfaces; i++) {
			BidirectionalInterface biInterface = new BidirectionalInterface();
			biInterface.setName(nodeIdName + " Interface" + i);

			Interface inboundInterface = new Interface();
			inboundInterface.setName(biInterface.getName() + " In");
			Interface outboundInterface = new Interface();
			outboundInterface.setName(biInterface.getName() + " Out");

			String biInterfaceIndName = this.createBiInterfaceIndividual(biInterface);
			String inInteIndName = this.createInterfaceIndividual(inboundInterface);
			String outInteIndName = this.createInterfaceIndividual(outboundInterface);

			ontomanager.createObjectPropertyAssertionAxiom(nodeIdName, inInteIndName,
					NamedObjectProp.HASINBOUNDINTERFACE);
			ontomanager.createObjectPropertyAssertionAxiom(nodeIdName, outInteIndName,
					NamedObjectProp.HASOUTBOUNDINTERFACE);

			ontomanager.createObjectPropertyAssertionAxiom(biInterfaceIndName, inInteIndName, NamedObjectProp.CONTAINS);
			ontomanager.createObjectPropertyAssertionAxiom(biInterfaceIndName, outInteIndName,
					NamedObjectProp.CONTAINS);
		}
	}
	
}
