package com.nschecker.repositories;

import org.semanticweb.owlapi.model.OWLDataFactory;

import com.nschecker.classes.Memory;
import com.nschecker.util.IndividualUtil;
import com.nschecker.util.NamedClasses;
import com.nschecker.util.NamedDataProp;

public class MemoryRepository {

	protected OntologyManager ontomanager;
	protected OWLDataFactory factory;

	public MemoryRepository(OntologyManager ontomanager) {
		this.ontomanager = ontomanager;
		this.factory = ontomanager.getFactory();		
	}
	
	public String createMemoryIndividual(Memory mem) {
		String memIndName = IndividualUtil.processNameForIndividual(mem.getName());
		ontomanager.createIndividual(memIndName, NamedClasses.MEMORY);
		ontomanager.createDataPropertyAssertionAxiom(memIndName, factory.getOWLLiteral(mem.getSize()),
				NamedDataProp.HASMEMORYSIZE);
		ontomanager.createDataPropertyAssertionAxiom(memIndName, factory.getOWLLiteral(mem.getAvailableSize()),
				NamedDataProp.HASAVAILABLEMEMORYSIZE);
		return memIndName;
	}
	
	public String createVMemoryIndividual(String vnfcName, int index) {
		String name = vnfcName + "-mem1gb-" + index; 
		String vMemIndName = IndividualUtil.processNameForIndividual(name);
		ontomanager.createIndividual(vMemIndName, NamedClasses.VMEMORY);
		//OWLNamedIndividual ind = ontomanager.createIndividual(vMemIndName, NamedClasses.VMEMORY);
		//ontomanager.makeDifferentFromOtherIndividuals(ind, NamedClasses.VMEMORY, ontomanager.createOWLReasoner());
		return vMemIndName;
	}
	
}
