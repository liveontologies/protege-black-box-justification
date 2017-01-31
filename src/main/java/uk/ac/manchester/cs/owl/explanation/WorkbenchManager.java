package uk.ac.manchester.cs.owl.explanation;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationGenerator;
import org.semanticweb.owl.explanation.api.ExplanationProgressMonitor;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge
 * Stanford University
 * Bio-Medical Informatics Research Group
 * Date: 20/03/2012
 */
public class WorkbenchManager {

	private WorkbenchSettings workbenchSettings = new WorkbenchSettings();
	private JustificationManager justificationManager;
	private OWLAxiom entailment;
	private ExplanationProgressMonitor<OWLAxiom> monitor;

	public WorkbenchManager(JustificationManager justificationManager, OWLAxiom entailment,
			ExplanationProgressMonitor<OWLAxiom> monitor) {
		this.justificationManager = justificationManager;
		this.entailment = entailment;
		this.monitor = monitor;
	}

	public ExplanationProgressMonitor<OWLAxiom> getMonitor() {
		return monitor;
	}

	public WorkbenchSettings getWorkbenchSettings() {
		return workbenchSettings;
	}

	public OWLAxiom getEntailment() {
		return entailment;
	}

	public Set<Explanation<OWLAxiom>> getJustifications(OWLAxiom entailment) {
		JustificationType justificationType = workbenchSettings.getJustificationType();
		return justificationManager.getJustifications(entailment, justificationType, getMonitor());
	}

	public int getJustificationCount(OWLAxiom entailment) {
		JustificationType justificationType = workbenchSettings.getJustificationType();
		return justificationManager.getComputedExplanationCount(entailment, justificationType);
	}

	public JustificationManager getJustificationManager() {
		return justificationManager;
	}

	public int getPopularity(OWLAxiom axiom) {
		int count = 0;
		Set<Explanation<OWLAxiom>> justifications = justificationManager.getJustifications(entailment,
				workbenchSettings.getJustificationType(), getMonitor());
		for (Explanation<OWLAxiom> justification : justifications) {
			if (justification.contains(axiom)) {
				count++;
			}
		}
		return count;
	}
}