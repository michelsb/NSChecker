package com.nschecker.evaluation.util;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.IRI;

public class OntoNFVUtilTest {

	public static final @Nonnull IRI NFV_IRI = IRI.create("http://cin.ufpe.br/msb6/ontologies/2018/1/", "onto-nfv.owl");
	public static final @Nonnull String ONTODIR = "resources/base-ontology/";
	public static final @Nonnull String ONTOTEMPDIR = "resources/created-ontologies/";
	public static final @Nonnull String GMLTOPODIR = "resources/topology-files/";
	public static final @Nonnull String RESULTDIR = "resources/result-files/";
	public static final @Nonnull String ONTOFILE = ONTODIR+"onto-nfv.owl";
	public static final @Nonnull String CONFIGFILE = "resources/config-files/config.properties";
	
}
