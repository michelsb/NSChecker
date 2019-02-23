package com.nschecker.modules;

import org.springframework.stereotype.Service;

import com.nschecker.classes.NFVI;
import com.nschecker.classes.NFVIPoP;
import com.nschecker.classes.Node;
import com.nschecker.dtos.NFVIPoPDto;
import com.nschecker.dtos.PhyConnBetweenHostsDto;
import com.nschecker.dtos.PhyConnBetweenPoPsDto;
import com.nschecker.dtos.NFVIDto;
import com.nschecker.reasoning.ConflictDetectionModule;
import com.nschecker.repositories.NFVINodeRepository;
import com.nschecker.repositories.NFVIPoPRepository;
import com.nschecker.repositories.NFVIRepository;
import com.nschecker.repositories.NetworkNodeRepository;
import com.nschecker.repositories.OntologyManager;
import com.nschecker.repositories.PhysicalConnectionRepository;
import com.nschecker.responses.ConsistencyResponse;
import com.nschecker.responses.TopologyResponse;
import com.nschecker.util.OntoNFVUtil;

@Service
public class NFVIResourceManagentModule {

	protected OntologyManager ontomanager;		
	protected NFVIRepository nfvi;
	protected NFVIPoPRepository nfviPop;
	protected NFVINodeRepository nfviNode;
	protected NetworkNodeRepository netNode;
	protected PhysicalConnectionRepository phyConn;
	protected ConflictDetectionModule cdm;
	protected boolean status;

	public NFVIResourceManagentModule() {
		this.ontomanager = new OntologyManager();
		this.status = false;
	}

	public void start() {
		this.ontomanager.startOntologyProcessing(OntoNFVUtil.ONTOFILE);		
		this.cdm = new ConflictDetectionModule(ontomanager);		
		this.nfvi = new NFVIRepository(this.ontomanager);
		this.nfviPop = new NFVIPoPRepository(this.ontomanager);
		this.nfviNode = new NFVINodeRepository(this.ontomanager);
		this.netNode = new NetworkNodeRepository(this.ontomanager);
		this.phyConn = new PhysicalConnectionRepository(this.ontomanager);
		this.status = true;
	}
	
	public void saveUpdates() {
		this.ontomanager.saveOntologyFile();
	}
	
	public void stop() {
		this.ontomanager.close();		
		this.cdm = null;
		this.nfvi = null;
		this.nfviPop = null;
		this.nfviNode = null;
		this.netNode = null;
		this.phyConn = null;
		this.status = false;
	}
	
	public TopologyResponse createTopology(NFVIDto nfviTopo) {		
		TopologyResponse topoResponse = new TopologyResponse();
		
		if (status) {
			NFVI nfvi = new NFVI();
			nfvi.setName(nfviTopo.getName());
			this.nfvi.createNFVIIndividual(nfvi);
	
			for (NFVIPoPDto nfviPoPTop : nfviTopo.getNfviPoPTops()) {
				NFVIPoP nfvipop = new NFVIPoP();
				nfvipop.setName(nfviPoPTop.getName());
				nfvipop.setNfvi(nfvi);
				this.nfviPop.createNFVIPoPIndividual(nfvipop);				
				for (Node nfvinode : nfviPoPTop.getNfviNodes()) {									
					nfvinode.setNfvipop(nfvipop);
					this.nfviNode.createNFVINodeIndividual(nfvinode);				
				}				
				for (Node switchnode : nfviPoPTop.getSwitchNodes()) {					
					switchnode.setNfvipop(nfvipop);				
					this.netNode.createNetworkNodeIndividual(switchnode);		
				}				
				for (PhyConnBetweenHostsDto conn : nfviPoPTop.getConnHosts()) {				
					this.phyConn.createPhysicalConnectionBetweenHosts(conn.getNameNode1(), conn.getInteIndexNode1(), 
							conn.getNameNode2(), conn.getInteIndexNode2(), conn.getSpeed());
				}
				
			}
			
			for (PhyConnBetweenPoPsDto conn : nfviTopo.getConnPoPs()) {
				this.phyConn.createPhysicalConnectionBetweenPoPs(conn.getNameNode1(), conn.getInteIndexNode1(), 
						conn.getNameNode2(), conn.getInteIndexNode2(), conn.getSpeed(), conn.getVxlanId());
			}			
			
			this.cdm.start();
			ConsistencyResponse consResponse = this.cdm.testOntoConsistency();
			
			if (consResponse.isConsistent()) {
				topoResponse.setCreated(true);
				topoResponse.setMessage("Topology created succesfully!");
				topoResponse.setConsistency(consResponse);
			} else {
				//topoResponse.setCreated(false);
				topoResponse.setMessage("ERROR: Topology was not created! Updates made Onto-NFV inconsistent!");
				topoResponse.setConsistency(consResponse);
			}			
		} else {
			//topoResponse.setCreated(false);
			topoResponse.setMessage("The service was not started!");
		}
		return topoResponse;
	}
	
	/*public TopologyResponse createTopology(NFVITopology nfviTopo) {		
		TopologyResponse topoResponse = new TopologyResponse();
		
		if (status) {
			NFVI nfvi = new NFVI();
			nfvi.setName(nfviTopo.getName());
			this.nfvi.createNFVIIndividual(nfvi);
	
			for (NFVIPoPTopology nfviPoPTop : nfviTopo.getNfviPoPTops()) {
				NFVIPoP nfvipop = new NFVIPoP();
				nfvipop.setName(nfviPoPTop.getName());
				nfvipop.setNfvi(nfvi);
				this.nfviPop.createNFVIPoPIndividual(nfvipop);
				
				for (Node node : nfviPoPTop.getNfviNodes()) {				
					CPU cpu = new CPU();
					cpu.setName(node.getCpu().getName());
					cpu.setNumCores(node.getCpu().getNumCores());
					cpu.setAvailableCores(node.getCpu().getAvailableCores());
					cpu.setSpeed(node.getCpu().getSpeed());
					
					Memory mem = new Memory();
					mem.setName(node.getMemory().getName());
					mem.setSize(node.getMemory().getSize());
					mem.setAvailableSize(node.getMemory().getAvailableSize());
					
					Storage sto = new Storage();
					sto.setName(node.getStorage().getName());
					sto.setSize(node.getStorage().getSize());
					sto.setAvailableSize(node.getStorage().getAvailableSize());				
					
					Node nfvinode = new Node();
					nfvinode.setName(node.getName());
					nfvinode.setCpu(cpu);
					nfvinode.setMemory(mem);
					nfvinode.setStorage(sto);
					nfvinode.setNumInterfaces(node.getNumInterfaces());
					nfvinode.setNfvipop(nfvipop);
					nfvinode.setMaxNumVNFs(node.getMaxNumVNFs());
					nfvinode.setMaxNumVCPUs(node.getMaxNumVCPUs());
					nfvinode.setMaxNumVMems(node.getMaxNumVMems());
					nfvinode.setMaxNumVStos(node.getMaxNumVStos());				
					this.nfviNode.createNFVINodeIndividual(nfvinode);				
				}
				
				for (Node node : nfviPoPTop.getSwitchNodes()) {				
					CPU cpu = new CPU();
					cpu.setName(node.getCpu().getName());
					cpu.setNumCores(node.getCpu().getNumCores());
					cpu.setAvailableCores(node.getCpu().getAvailableCores());
					cpu.setSpeed(node.getCpu().getSpeed());
					
					Memory mem = new Memory();
					mem.setName(node.getMemory().getName());
					mem.setSize(node.getMemory().getSize());
					mem.setAvailableSize(node.getMemory().getAvailableSize());
					
					Storage sto = new Storage();
					sto.setName(node.getStorage().getName());
					sto.setSize(node.getStorage().getSize());
					sto.setAvailableSize(node.getStorage().getAvailableSize());				
					
					SwitchingMatrix swt = new SwitchingMatrix();
					swt.setName(node.getSwitchingMatrix().getName());
					
					Node switchnode = new Node();				
					switchnode.setName(node.getName());
					switchnode.setCpu(cpu);
					switchnode.setMemory(mem);
					switchnode.setStorage(sto);
					switchnode.setSwitchingMatrix(swt);
					switchnode.setNumInterfaces(node.getNumInterfaces());
					switchnode.setNfvipop(nfvipop);				
					this.netNode.createNetworkNodeIndividual(switchnode);		
				}
				
				for (PhyConnBetweenHosts conn : nfviPoPTop.getConnHosts()) {				
					this.phyConn.createPhysicalConnectionBetweenHosts(conn.getNameNode1(), conn.getInteIndexNode1(), 
							conn.getNameNode2(), conn.getInteIndexNode2(), conn.getSpeed());
				}
				
			}
			
			for (PhyConnBetweenPoPs conn : nfviTopo.getConnPoPs()) {
				this.phyConn.createPhysicalConnectionBetweenPoPs(conn.getNameNode1(), conn.getInteIndexNode1(), 
						conn.getNameNode2(), conn.getInteIndexNode2(), conn.getSpeed(), conn.getVxlanId());
			}			
			
			this.cdm.start();
			ConsistencyResponse consResponse = this.cdm.testOntoConsistency();
			
			if (consResponse.isConsitent()) {
				topoResponse.setCreated(true);
				topoResponse.setMessage("Topology created succesfully!");
				topoResponse.setConsistency(consResponse);
			} else {
				//topoResponse.setCreated(false);
				topoResponse.setMessage("ERROR: Topology was not created! Updates made Onto-NFV inconsistent!");
				topoResponse.setConsistency(consResponse);
			}			
		} else {
			//topoResponse.setCreated(false);
			topoResponse.setMessage("The service was not started!");
		}
		return topoResponse;
	}*/
	
}
