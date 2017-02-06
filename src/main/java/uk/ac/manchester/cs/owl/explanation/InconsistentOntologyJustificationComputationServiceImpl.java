package uk.ac.manchester.cs.owl.explanation;

import org.semanticweb.owlapi.model.OWLAxiom;

import not.org.saa.protege.explanation.joint.service.InconsistentOntologyJustificationComputationService;
import not.org.saa.protege.explanation.joint.service.JustificationComputation;

public class InconsistentOntologyJustificationComputationServiceImpl extends InconsistentOntologyJustificationComputationService {

	@Override
	public JustificationComputation creareComputation(OWLAxiom entailment) {
		return new JustificationComputator(entailment, getOWLEditorKit());
	}

	@Override
	public void initialise() throws Exception {
	}

	@Override
	public void dispose() throws Exception {
	}
}