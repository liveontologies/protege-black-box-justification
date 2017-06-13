package org.liveontologies.protege.justification.blackbox;

/*-
 * #%L
 * Protege Blackbox Justification Explanation
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2016 - 2017 Live Ontologies Project
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
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

	private final WorkbenchManager workbenchManager;

	private static final Logger logger = LoggerFactory
			.getLogger(WorkbenchLogic.class);

	public WorkbenchLogic(OWLEditorKit ek, OWLAxiom entailment,
			ExplanationProgressMonitor<OWLAxiom> monitor,
			WorkbenchSettings workbenchSettings) {
		JFrame workspaceFrame = ProtegeManager.getInstance()
				.getFrame(ek.getWorkspace());
		JustificationManager justificationManager = JustificationManager
				.getExplanationManager(workspaceFrame, ek.getOWLModelManager());
		this.workbenchManager = new WorkbenchManager(justificationManager,
				entailment, monitor, workbenchSettings);
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
			logger.error("An error occurred whilst computing explanations: {}",
					e.getMessage(), e);
			return null;
		}
	}

	public void startComputation() {
		workbenchManager.getJustifications(workbenchManager.getEntailment());
	}

	public List<Explanation<OWLAxiom>> getExplanations() {
		try {
			List<Explanation<OWLAxiom>> explanations = getOrderedExplanations(
					workbenchManager.getJustifications(
							workbenchManager.getEntailment()));
			return explanations;
		} catch (ExplanationException e) {
			logger.error("An error occurred whilst computing explanations: {}",
					e.getMessage(), e);
			return null;
		}
	}

	protected List<Explanation<OWLAxiom>> getOrderedExplanations(
			Set<Explanation<OWLAxiom>> explanations) {
		List<Explanation<OWLAxiom>> orderedExplanations = new ArrayList<>();
		orderedExplanations.addAll(explanations);
		Collections.sort(orderedExplanations,
				new Comparator<Explanation<OWLAxiom>>() {
					public int compare(Explanation<OWLAxiom> o1,
							Explanation<OWLAxiom> o2) {
						int diff = getAxiomTypes(o1).size()
								- getAxiomTypes(o2).size();
						if (diff != 0) {
							return diff;
						}
						diff = getClassExpressionTypes(o1).size()
								- getClassExpressionTypes(o2).size();
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

	private Set<ClassExpressionType> getClassExpressionTypes(
			Explanation<OWLAxiom> explanation) {
		Set<ClassExpressionType> result = new HashSet<>();
		for (OWLAxiom ax : explanation.getAxioms()) {
			result.addAll(ax.getNestedClassExpressions().stream()
					.map(OWLClassExpression::getClassExpressionType)
					.collect(Collectors.toList()));
		}
		return result;
	}

	public void dispose() {
	}
}