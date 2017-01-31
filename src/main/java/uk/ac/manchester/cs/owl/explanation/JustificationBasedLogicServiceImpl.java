package uk.ac.manchester.cs.owl.explanation;

import org.semanticweb.owlapi.model.OWLAxiom;

import not.org.saa.protege.explanation.joint.service.JustificationComputation;
import not.org.saa.protege.explanation.joint.service.JustificationComputationService;

public class JustificationBasedLogicServiceImpl extends JustificationComputationService {

	@Override
	public void initialise() throws Exception {
	}

	@Override
	public boolean hasAxioms(OWLAxiom entailment) {
		return true;
	}

	@Override
	public void dispose() {
	}

	@Override
	public JustificationComputation creareComputation(OWLAxiom entailment) {
		return new JustificationComputator(entailment, getOWLEditorKit());
	}
}
