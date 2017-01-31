package uk.ac.manchester.cs.owl.explanation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationGenerator;
import org.semanticweb.owl.explanation.api.ExplanationProgressMonitor;
import org.semanticweb.owlapi.model.OWLAxiom;

import not.org.saa.protege.explanation.joint.service.JustificationComputationListener;
import not.org.saa.protege.explanation.joint.service.JustificationComputation;

public class JustificationComputator extends JustificationComputation {

	private boolean isInterrupted = false;
	private List<JustificationComputationListener> listeners;
	private WorkbenchLogic logic;

	public JustificationComputator(OWLAxiom entailment, OWLEditorKit kit) {
		super(entailment);
		listeners = new ArrayList<JustificationComputationListener>();
		JustificationProgressMonitor monitor = new JustificationProgressMonitor(this);
		logic = new WorkbenchLogic(kit, entailment, monitor);
	}

	@Override
	public void startComputation() {
		logic.startComputation();
	}

	private void foundExplanation(ArrayList<OWLAxiom> explanation) {
		for (JustificationComputationListener listener : new ArrayList<>(listeners))
			listener.foundJustification(explanation);
	}

	@Override
	public void interruptComputation() {
		isInterrupted = true;
	}

	@Override
	public boolean isComputationInterrupted() {
		return isInterrupted;
	}

	@Override
	public void addComputationListener(JustificationComputationListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeComputationListener(JustificationComputationListener listener) {
		listeners.remove(listener);
	}

	private class JustificationProgressMonitor implements ExplanationProgressMonitor<OWLAxiom> {

		private final JustificationComputator computation;

		public JustificationProgressMonitor(JustificationComputator computation) {
			this.computation = computation;
		}

		public void foundExplanation(ExplanationGenerator<OWLAxiom> owlAxiomExplanationGenerator,
				Explanation<OWLAxiom> explanation, Set<Explanation<OWLAxiom>> explanations) {
			ArrayList<OWLAxiom> list = new ArrayList<OWLAxiom>();
			for (OWLAxiom axiom : explanation.getAxioms())
				list.add(axiom);
			computation.foundExplanation(list);
		}

		public boolean isCancelled() {
			return computation.isComputationInterrupted();
		}
	}
}