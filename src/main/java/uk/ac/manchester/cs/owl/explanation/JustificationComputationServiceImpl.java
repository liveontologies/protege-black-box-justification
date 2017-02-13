package uk.ac.manchester.cs.owl.explanation;

import org.semanticweb.owlapi.model.OWLAxiom;

import not.org.saa.protege.explanation.joint.service.JustificationComputation;
import not.org.saa.protege.explanation.joint.service.ComputationService;

public class JustificationComputationServiceImpl extends ComputationService {

	@Override
	public void initialise() throws Exception {
	}

	@Override
	public boolean canComputeJustification(OWLAxiom entailment) {
		return true;
	}

	@Override
	public void dispose() {
	}

	@Override
	public JustificationComputation createJustificationComputation(OWLAxiom entailment) {
		return new JustificationComputator(entailment, getOWLEditorKit());
	}

	@Override
	public String getName() {
		return "Workbench Justification Service";
	}
}
