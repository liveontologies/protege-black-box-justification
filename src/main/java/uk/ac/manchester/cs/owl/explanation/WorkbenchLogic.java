package uk.ac.manchester.cs.owl.explanation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JFrame;

import org.protege.editor.core.ProtegeManager;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationException;
import org.semanticweb.owl.explanation.api.ExplanationProgressMonitor;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkbenchLogic {
	private OWLEditorKit editorKit;

	private final WorkbenchManager workbenchManager;

	private static final Logger logger = LoggerFactory.getLogger(WorkbenchLogic.class);

	public WorkbenchLogic(OWLEditorKit ek, OWLAxiom entailment, ExplanationProgressMonitor<OWLAxiom> monitor, WorkbenchSettings workbenchSettings) {
		this.editorKit = ek;
		JFrame workspaceFrame = ProtegeManager.getInstance().getFrame(ek.getWorkspace());
		JustificationManager justificationManager = JustificationManager.getExplanationManager(workspaceFrame,
				ek.getOWLModelManager());
		this.workbenchManager = new WorkbenchManager(justificationManager, entailment, monitor, workbenchSettings);
	}

	public HashSet<ArrayList<OWLAxiom>> getAxioms() {
		try {
			List<Explanation<OWLAxiom>> explanations = getExplanations();
			HashSet<ArrayList<OWLAxiom>> axioms = new HashSet<ArrayList<OWLAxiom>>();
			for (int i = 0; i < explanations.size(); i++) {
				ArrayList<OWLAxiom> list = new ArrayList<OWLAxiom>();
				for (OWLAxiom explanation : explanations.get(i).getAxioms())
					list.add(explanation);
				axioms.add(list);
			}
			return axioms;
		} catch (ExplanationException e) {
			logger.error("An error occurred whilst computing explanations: {}", e.getMessage(), e);
			return null;
		}
	}

	public void startComputation() {
		workbenchManager.getJustifications(workbenchManager.getEntailment());
	}

	public List<Explanation<OWLAxiom>> getExplanations() {
		try {
			List<Explanation<OWLAxiom>> explanations = getOrderedExplanations(
					workbenchManager.getJustifications(workbenchManager.getEntailment()));
			return explanations;
		} catch (ExplanationException e) {
			logger.error("An error occurred whilst computing explanations: {}", e.getMessage(), e);
			return null;
		}
	}

	protected List<Explanation<OWLAxiom>> getOrderedExplanations(Set<Explanation<OWLAxiom>> explanations) {
		List<Explanation<OWLAxiom>> orderedExplanations = new ArrayList<>();
		orderedExplanations.addAll(explanations);
		Collections.sort(orderedExplanations, new Comparator<Explanation<OWLAxiom>>() {
			public int compare(Explanation<OWLAxiom> o1, Explanation<OWLAxiom> o2) {
				int diff = getAxiomTypes(o1).size() - getAxiomTypes(o2).size();
				if (diff != 0) {
					return diff;
				}
				diff = getClassExpressionTypes(o1).size() - getClassExpressionTypes(o2).size();
				if (diff != 0) {
					return diff;
				}
				return o1.getSize() - o2.getSize();
			}
		});
		return orderedExplanations;
	}

	private Set<AxiomType<?>> getAxiomTypes(Explanation<OWLAxiom> explanation) {
		Set<AxiomType<?>> result = new HashSet<>();
		for (OWLAxiom ax : explanation.getAxioms()) {
			result.add(ax.getAxiomType());
		}
		return result;
	}

	private Set<ClassExpressionType> getClassExpressionTypes(Explanation<OWLAxiom> explanation) {
		Set<ClassExpressionType> result = new HashSet<>();
		for (OWLAxiom ax : explanation.getAxioms()) {
			result.addAll(ax.getNestedClassExpressions().stream().map(OWLClassExpression::getClassExpressionType)
					.collect(Collectors.toList()));
		}
		return result;
	}

	public void dispose() {
	}
}