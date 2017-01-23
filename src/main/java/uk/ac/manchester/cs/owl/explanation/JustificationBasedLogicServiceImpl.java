package uk.ac.manchester.cs.owl.explanation;

import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;

import not.org.saa.protege.explanation.joint.service.AxiomsProgressMonitor;
import not.org.saa.protege.explanation.joint.service.LogicService;

public class JustificationBasedLogicServiceImpl extends LogicService {

	// there should be singleton "private WorkbenchLogic logic;" which would
	// return "hasAxiom" also but whatever

	@Override
	public void initialise() throws Exception {
	}

	@Override
	public void startComputation(OWLAxiom entailment, AxiomsProgressMonitor<OWLAxiom> monitor) {
		WorkbenchLogic logic = new WorkbenchLogic(getOWLEditorKit(), entailment, monitor);
		logic.startComputation(monitor);
	}

	// public Set<? extends List<OWLAxiom>> getAxioms(OWLAxiom entailment) {
	// WorkbenchLogic logic = new WorkbenchLogic(getOWLEditorKit(), entailment);
	// return logic.getAxioms();
	// }

	@Override
	public boolean hasAxioms(OWLAxiom entailment) {
		return true;
	}

	@Override
	public void dispose() {
	}
}
